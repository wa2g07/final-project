package it.polito.wa2.g07.ticketcatalogueservice.repositories

import it.polito.wa2.g07.ticketcatalogueservice.documents.Ticket
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketCatalogRepository : ReactiveMongoRepository<Ticket, ObjectId>
