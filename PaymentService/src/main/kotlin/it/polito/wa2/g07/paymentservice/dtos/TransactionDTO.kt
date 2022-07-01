package it.polito.wa2.g07.paymentservice.dtos

import it.polito.wa2.g07.paymentservice.documents.Transaction

data class TransactionDTO(val id: String = "",
                          val owner: String = "",
                          val totalCost: Double = 0.0,
                          val ticketsAmount: Int = 0,
                          val ticketId: String = "")

fun Transaction.toDTO(): TransactionDTO {
    return TransactionDTO(this.id.toHexString(), this.owner, this.totalCost,this.ticketsAmount,this.ticketId)
}
