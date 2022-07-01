package it.polito.wa2.g07.ticketcatalogueservice.dtos

import it.polito.wa2.g07.ticketcatalogueservice.documents.Ticket

data class TicketDTO(val id : String, val price: Float, val type : String, val ageRestriction: String?, val zones: String)

fun Ticket.toDTO(): TicketDTO{
    return TicketDTO(id = this.id.toHexString(), price = this.price, type = this.type, ageRestriction = this.ageRestriction, zones = this.zones)
}
