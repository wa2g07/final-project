package it.polito.wa2.g07.dtos

import it.polito.wa2.g07.entities.Activation

data class ActivationDTO(val id: String? = null,val attempts_counter: Int? = null, val activationCode: String = "", val userId: Long? = null,val userEmail: String? = null)
fun Activation.toDTO(): ActivationDTO {
    return ActivationDTO(id = this.id.toString(),attempts_counter = this.attemptsCounter,activationCode = this.activationCode, userId = this.user?.id, userEmail = this.user?.email)
}