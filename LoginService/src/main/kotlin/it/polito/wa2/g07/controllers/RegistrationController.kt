package it.polito.wa2.g07.controllers

import it.polito.wa2.g07.dtos.ActivationDTO
import it.polito.wa2.g07.dtos.UserDTO
import it.polito.wa2.g07.services.UserService
import it.polito.wa2.g07.utils.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
class RegistrationController(val service: UserService) {

    @PostMapping("/user/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun registration(@RequestBody body: RegistrationUser): RegistrationResponse {
        try {
            val activationDTO =
                service.registerUser(
                    UserDTO(
                        username = body.nickname,
                        email = body.email,
                        password = body.password
                    )
                )

            return RegistrationResponse(
                    provisional_id = activationDTO.id!!, email = activationDTO.userEmail!!
            )

        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/user/validate")
    @ResponseStatus(HttpStatus.CREATED)
    fun validation(@RequestBody body: ActivationUser): ValidationResponse {
        try {
            val userDTO = service.validateActivationRequest(ActivationDTO(activationCode = body.activation_code, id = body.provisional_id))
            return ValidationResponse(
                userId = userDTO!!.id!!, nickname = userDTO.username, email = userDTO.email
            )
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/user/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody body: LoginUser): String {

        try {
            val jwt = service.loginUser(UserDTO(username = body.username, password = body.password))

            return jwt
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN)
        }
    }


}
