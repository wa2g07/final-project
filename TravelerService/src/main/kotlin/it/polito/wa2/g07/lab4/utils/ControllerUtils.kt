package it.polito.wa2.g07.lab4.utils

import java.util.*

data class ProfileResponse(val username: String,
                           val name: String?,
                           val address: String?,
                           val telephone: String?,
                           val dateOfBirth: Date?)

data class TicketResponse(val sub: Long,
                              val zId: String,
                              val iat: String,
                             val exp: String,
                              val jws: String)

data class UpdateProfileData(val name: String?,
                             val address: String?,
                             val telephone: String?,
                             val dateOfBirth: Date?)

data class BuyTicketRequest(val cmd: String?,
                            val quantity: Int?,
                            val zones: String?)