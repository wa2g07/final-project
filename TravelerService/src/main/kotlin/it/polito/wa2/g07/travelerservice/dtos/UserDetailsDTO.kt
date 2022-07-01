package it.polito.wa2.g07.travelerservice.dtos

import it.polito.wa2.g07.travelerservice.entities.UserDetails
import java.util.*

data class UserDetailsDTO(
  val id: Long? = null,
  val username: String,
  val name: String?,
  val address: String?,
  val telephone: String?,
  val dateOfBirth: Date?,
)

fun UserDetails.toDTO(): UserDetailsDTO {
  return UserDetailsDTO(
    this.id, this.username, this.name, this.address, this.telephone, this.dateOfBirth
  )
}
