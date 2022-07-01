package it.polito.wa2.g07.lab4.dtos

import it.polito.wa2.g07.lab4.entities.UserDetails
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
