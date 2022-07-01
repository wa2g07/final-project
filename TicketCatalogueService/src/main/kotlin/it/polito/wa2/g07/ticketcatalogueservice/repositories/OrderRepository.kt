package it.polito.wa2.g07.ticketcatalogueservice.repositories

import it.polito.wa2.g07.ticketcatalogueservice.documents.Order
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface OrderRepository: ReactiveMongoRepository<Order, ObjectId> {
  fun findAllByUsername(username: String): Flux<Order>
}