package it.polito.wa2.g07.dtos

import it.polito.wa2.g07.entities.User

data class UserDTO(val id: Long? = null,val password: String = "", val username: String = "",val email: String = "",val activationId: String? = null)

fun User.toDTO(): UserDTO {
    return UserDTO(this.id, this.password, this.username,this.email,this.activation?.id.toString())
}
