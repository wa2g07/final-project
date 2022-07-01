package it.polito.wa2.g07.ticketcatalogueservice.services

import it.polito.wa2.g07.ticketcatalogueservice.documents.Order
import it.polito.wa2.g07.ticketcatalogueservice.documents.Ticket
import it.polito.wa2.g07.ticketcatalogueservice.dtos.OrderDTO
import it.polito.wa2.g07.ticketcatalogueservice.dtos.TicketDTO
import it.polito.wa2.g07.ticketcatalogueservice.dtos.toDTO
import it.polito.wa2.g07.ticketcatalogueservice.repositories.OrderRepository
import it.polito.wa2.g07.ticketcatalogueservice.repositories.TicketCatalogRepository
import it.polito.wa2.g07.ticketcatalogueservice.utils.TicketType
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TicketCatalogServiceImpl(val ticketCatalogRepository: TicketCatalogRepository, val orderRepository: OrderRepository) :
    TicketCatalogService {
  override fun getAllTicketTypes(): Flux<TicketDTO> {
    return ticketCatalogRepository.findAll().map{it.toDTO()}
  }

  override suspend fun getOrdersByUsername(username: Mono<String>): Flux<OrderDTO> {
    return orderRepository.findAllByUsername(username.awaitSingle()).map { it.toDTO() }
  }

  override fun getAllOrders(): Flux<OrderDTO> {
    return orderRepository.findAll().map { it.toDTO() }
  }

  override suspend fun getOrderById(id: String, username: Mono<String>): Mono<OrderDTO> {
    val order = orderRepository.findById(ObjectId(id))
    if(order.awaitSingleOrNull()?.username?.compareTo(username.awaitSingle()) == 0)
      return order.map { it.toDTO() }
    else
      throw Exception()
  }

  override fun addNewTicketType(ticketType: TicketType): Mono<TicketDTO> {
    val ticket = Ticket(price = ticketType.price, type = ticketType.type, ageRestriction = ticketType.ageRestriction, zones = ticketType.zones)
    return ticketCatalogRepository.save(ticket).map { it.toDTO() }
  }

  override fun getTicketTypeById(id: String): Mono<TicketDTO> {
    return ticketCatalogRepository.findById(ObjectId(id)).map { it.toDTO() }
  }

  override fun updateOrderStatus(id: String, status: String) : Mono<OrderDTO> {
    val order = orderRepository.findById(ObjectId(id)).block()
    order!!.status = status
    return orderRepository.save(order).map { it.toDTO() }
  }

  override suspend fun addOrder(orderDTO: OrderDTO): Mono<OrderDTO> {

    return orderRepository.save(Order(
            username = orderDTO.username,
            ticketId = ticketCatalogRepository.findById(ObjectId(orderDTO.ticketId))
                    .awaitSingle(),
            ticketsNumber = orderDTO.ticketsNumber
    )).map { it.toDTO() }
  }
}