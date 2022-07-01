package it.polito.wa2.g07.ticketcatalogueservice.dtos

import it.polito.wa2.g07.ticketcatalogueservice.documents.Order

data class OrderDTO(val id : String = "", val username: String, val ticketsNumber : Int, val ticketId: String, val status: String = "")

fun Order.toDTO(): OrderDTO{
    return OrderDTO(id = this.id.toHexString(), username = this.username, ticketsNumber = this.ticketsNumber, ticketId = this.ticketId.id.toHexString(), status = this.status)
}
