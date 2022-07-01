package it.polito.wa2.g07.ticketcatalogueservice.utils

data class TicketType (val price: Float,
                       val type: String,
                       val ageRestriction: String?,
                       val zones: String)

data class BuyTicketsRequest (val ticketsQuantity: Int,
                              val creditCardNumber: String,
                              val expDate: String,
                              val cvv: Int,
                              val cardHolder: String)

data class ProfileResponse(val username: String,
                           val name: String?,
                           val address: String?,
                           val telephone: String?,
                           val dateOfBirth: String?)

data class TicketResponse(val sub: Long,
                          val zId: String,
                          val iat: String,
                          val exp: String,
                          val jws: String)

data class BuyTicketRequest(val cmd: String?,
                            val quantity: Int?,
                            val zones: String?)