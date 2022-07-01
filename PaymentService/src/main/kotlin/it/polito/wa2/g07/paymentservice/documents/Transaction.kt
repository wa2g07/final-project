package it.polito.wa2.g07.paymentservice.documents

import it.polito.wa2.g07.paymentservice.dtos.TransactionDTO
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Transaction(
  @Id val id: ObjectId = ObjectId.get(),
  val owner: String,
  val totalCost: Double,
  val ticketsAmount: Int,
  val ticketId: String
)
