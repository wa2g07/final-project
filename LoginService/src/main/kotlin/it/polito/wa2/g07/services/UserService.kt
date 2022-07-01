package it.polito.wa2.g07.services

import it.polito.wa2.g07.dtos.ActivationDTO
import it.polito.wa2.g07.dtos.UserDTO

interface UserService {
  fun registerUser(userDTO:UserDTO): ActivationDTO
  fun validateActivationRequest(activationDTO: ActivationDTO) : UserDTO?
  fun loginUser(userDTO: UserDTO): String
}