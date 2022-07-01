package it.polito.wa2.g07.ticketcatalogueservice.services

import it.polito.wa2.g07.ticketcatalogueservice.dtos.OrderDTO
import it.polito.wa2.g07.ticketcatalogueservice.dtos.TicketDTO
import it.polito.wa2.g07.ticketcatalogueservice.utils.TicketType
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TicketCatalogService {
  fun getAllTicketTypes(): Flux<TicketDTO>
  suspend fun getOrdersByUsername(username: Mono<String>): Flux<OrderDTO>
  fun getAllOrders(): Flux<OrderDTO>
  suspend fun getOrderById(id: String, username: Mono<String>) : Mono<OrderDTO>
  fun addNewTicketType(ticketType: TicketType): Mono<TicketDTO>
  fun getTicketTypeById(id: String) : Mono<TicketDTO>
  fun updateOrderStatus(id: String, status: String) : Mono<OrderDTO>
  suspend fun addOrder(orderDTO: OrderDTO) : Mono<OrderDTO>
}