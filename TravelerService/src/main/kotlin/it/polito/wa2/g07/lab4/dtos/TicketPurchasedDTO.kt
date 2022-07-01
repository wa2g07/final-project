package it.polito.wa2.g07.lab4.dtos

import it.polito.wa2.g07.lab4.entities.TicketPurchased

data class TicketPurchasedDTO(val id: Long?,
                              val exp: String = "",
                              val zId: String,
                              val iat: String = "",
                              val jws: String = "",
                              val userId: Long?=null)

fun TicketPurchased.toDTO(): TicketPurchasedDTO{
    return TicketPurchasedDTO(id = this.id,
            userId = this.user!!.id,
            iat = this.iat.toString(),
            exp = this.exp.toString(),
            jws = this.jws,
            zId = this.zones
            )
}