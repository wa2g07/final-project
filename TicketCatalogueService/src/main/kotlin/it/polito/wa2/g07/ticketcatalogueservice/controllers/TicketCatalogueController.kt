package it.polito.wa2.g07.ticketcatalogueservice.controllers

import it.polito.wa2.g07.ticketcatalogueservice.dtos.OrderDTO
import it.polito.wa2.g07.ticketcatalogueservice.dtos.TicketDTO
import it.polito.wa2.g07.ticketcatalogueservice.kafka.BillingInformation
import it.polito.wa2.g07.ticketcatalogueservice.security.JwtUtils
import it.polito.wa2.g07.ticketcatalogueservice.services.TicketCatalogService
import it.polito.wa2.g07.ticketcatalogueservice.utils.BuyTicketsRequest
import it.polito.wa2.g07.ticketcatalogueservice.utils.ProfileResponse
import it.polito.wa2.g07.ticketcatalogueservice.utils.TicketType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.time.LocalDate
import javax.annotation.PostConstruct

@RestController
class TicketCatalogueController(
  val ticketCatalogService: TicketCatalogService,
  @Autowired
  private val kafkaTemplate: KafkaTemplate<String, Any>,
  @Autowired
  private val jwtUtils: JwtUtils
) {

  @Value("\${kafka.topics.order}")
  var topic: String = ""

  @Value("\${travelerService.address}")
  var travelerServiceAddress: String = ""

  lateinit var webClientTraveler: WebClient

  @PostConstruct
  fun initWebClient(){
    webClientTraveler = WebClient.create(travelerServiceAddress)
  }
  private val log = LoggerFactory.getLogger(javaClass)


  @GetMapping(value = ["/tickets"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
  @ResponseStatus(HttpStatus.OK)
  fun getTickets(): Flow<TicketDTO> {
    return ticketCatalogService.getAllTicketTypes().asFlow()
  }

  @PostMapping("/shop/{ticketId}")
  @ResponseStatus(HttpStatus.OK)
  suspend fun buyTickets(@RequestHeader header : Map<String, String>, @PathVariable ticketId: String, @RequestBody body : BuyTicketsRequest, @AuthenticationPrincipal principal: Mono<String>): Flow<OrderDTO> {

    if(body.ticketsQuantity < 1)
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one ticket must be bought")

    val ticketType = ticketCatalogService.getTicketTypeById(ticketId).awaitSingleOrNull()
        ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket id not valid")

    if(ticketType.ageRestriction != null) {
      val authHeader = header[HttpHeaders.AUTHORIZATION]
      val res = webClientTraveler.get().uri("/my/profile").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, authHeader).retrieve().awaitBodyOrNull<ProfileResponse>()
      val dob: LocalDate
      if (res != null) {
        if(res.dateOfBirth != null) {
          dob = LocalDate.parse(res.dateOfBirth)
        }
        else
          throw ResponseStatusException(HttpStatus.BAD_REQUEST, "You have to set your birth date")
      }
      else
        throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot retrieve your age")
      val array = ticketType.ageRestriction.toCharArray()
      val ageLimit = array[0].digitToInt() * 10 + array[1].digitToInt()
      val sign = array[2]
      if(sign == 'p' && dob.isAfter(LocalDate.now().minusYears(ageLimit.toLong())))
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not old enough to buy this ticket type")
      if(sign == 'm' && dob.isBefore(LocalDate.now().minusYears(ageLimit.toLong())))
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not young enough to buy this ticket type")

    }

    try {
    val jwt = header[HttpHeaders.AUTHORIZATION]!!.substring(7)
    val order = ticketCatalogService.addOrder(OrderDTO(
            username = jwtUtils.getDetailsJwt(jwt),
            ticketsNumber = body.ticketsQuantity,
            ticketId = ticketId
    ))

    val bi = BillingInformation(order.map { it.id }.awaitSingle(),body.ticketsQuantity,
            ticketId, body.creditCardNumber, body.expDate, body.cvv.toString(), principal.awaitSingle(),
            body.ticketsQuantity*ticketType.price.toDouble(), jwt )

      log.info("Receiving buy tickets request")
      log.info("Sending message to Kafka {}", bi)
      val message: org.springframework.messaging.Message<BillingInformation> = MessageBuilder
        .withPayload(bi)
        .setHeader(KafkaHeaders.TOPIC, topic)
        .build()
      kafkaTemplate.send(message)
      log.info("Message sent with success")
      return order.asFlow()
    } catch (e: Exception){
      throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }
  }

  @GetMapping(value = ["/orders"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
  @ResponseStatus(HttpStatus.OK)
  suspend fun getMyOrders(@AuthenticationPrincipal principal: Mono<String>): Flow<OrderDTO> {
    return ticketCatalogService.getOrdersByUsername(principal).asFlow()
  }

  @GetMapping(value = ["/orders/{orderId}"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
  @ResponseStatus(HttpStatus.OK)
  suspend fun getMyOrderById(
    @PathVariable orderId: String,
    @AuthenticationPrincipal principal: Mono<String>
  ): Flow<OrderDTO> {
    try {
      return ticketCatalogService.getOrderById(orderId, principal).asFlow()
    } catch (e: Exception) {
      throw ResponseStatusException(HttpStatus.FORBIDDEN)
    }
  }

  @PostMapping(value = ["/admin/tickets"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
  @ResponseStatus(HttpStatus.CREATED)
  fun addNewTickets(@RequestBody body: TicketType): Flow<TicketDTO> {
    if (body.ageRestriction != null)
      if (!body.ageRestriction.matches("^[0-9][0-9][pm]$".toRegex()))
        throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    return ticketCatalogService.addNewTicketType(body).asFlow()
  }

  @GetMapping(value = ["/admin/orders"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
  @ResponseStatus(HttpStatus.OK)
  fun getAllOrders(): Flow<OrderDTO> {
    return ticketCatalogService.getAllOrders().asFlow()
  }

  @GetMapping(value = ["/admin/orders/{username}"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
  @ResponseStatus(HttpStatus.OK)
  suspend fun getUserOrdersById(@PathVariable username: String): Flow<OrderDTO> {
    return ticketCatalogService.getOrdersByUsername(username.toMono()).asFlow()
  }
}