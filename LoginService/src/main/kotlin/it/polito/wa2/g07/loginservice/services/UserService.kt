package it.polito.wa2.g07.loginservice.services

import it.polito.wa2.g07.loginservice.dtos.ActivationDTO
import it.polito.wa2.g07.loginservice.dtos.UserDTO
import it.polito.wa2.g07.loginservice.utils.Role

interface UserService {
  fun registerAdmin(userDTO: UserDTO, superadminName: String, userRole: Role): UserDTO
  fun registerUser(userDTO: UserDTO): ActivationDTO
  fun validateActivationRequest(activationDTO: ActivationDTO) : UserDTO?
  fun loginUser(userDTO: UserDTO): String
}