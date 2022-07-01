package it.polito.wa2.g07.ticketcatalogueservice.documents

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Order(
        @Id val id: ObjectId = ObjectId.get(),
        val username: String,
        val ticketsNumber: Int,
        val ticketId: Ticket,
        var status: String = "PENDING"
)
