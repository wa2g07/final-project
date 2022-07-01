package it.polito.wa2.g07.ticketcatalogueservice.kafka

import it.polito.wa2.g07.ticketcatalogueservice.security.JwtUtils
import it.polito.wa2.g07.ticketcatalogueservice.services.TicketCatalogService
import it.polito.wa2.g07.ticketcatalogueservice.utils.BuyTicketRequest
import it.polito.wa2.g07.ticketcatalogueservice.utils.TicketResponse
import jdk.jfr.ContentType
import kotlinx.coroutines.reactor.awaitSingle
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.client.toEntity
import javax.annotation.PostConstruct

@Component
class Consumer {
  private val logger = LoggerFactory.getLogger(javaClass)

  @Autowired
  lateinit var ticketService: TicketCatalogService

  @Value("\${travelerService.address}")
  var travelerServiceAddress: String = ""

  lateinit var webClientTraveler: WebClient

  @PostConstruct
  fun initWebClient(){
    webClientTraveler = WebClient.create(travelerServiceAddress)
  }

  @KafkaListener(topics = ["outcome"], groupId = "ppr2")
  fun listener(consumerRecord: ConsumerRecord<Any, Any>, ack: Acknowledgment) {
    logger.info("Message received {}", consumerRecord)
    ack.acknowledge()
    val outcome = consumerRecord.value() as Outcome
    if (outcome.result == "success") {
      ticketService.updateOrderStatus(outcome.orderId, "COMPLETED").block()
      val authHeader = "Bearer ${outcome.jwt}"
      val zones = ticketService.getTicketTypeById(outcome.ticketId).block()!!.zones
      val body = "{ \"cmd\": \"buy_tickets\", \"quantity\": \"${outcome.ticketsAmount}\", \"zones\": \"${zones}\"}"
      val res = webClientTraveler.post().
      uri("/my/tickets").
      accept(MediaType.APPLICATION_JSON).
      header(HttpHeaders.AUTHORIZATION, authHeader).
              header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON.toString()).
      bodyValue(BuyTicketRequest("buy_tickets",outcome.ticketsAmount,zones))
              .retrieve().toBodilessEntity().block()
    } else {
      ticketService.updateOrderStatus(outcome.orderId, "REJECTED").block()
    }
  }
}