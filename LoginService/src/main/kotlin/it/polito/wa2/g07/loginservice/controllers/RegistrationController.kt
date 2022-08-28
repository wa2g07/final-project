package it.polito.wa2.g07.loginservice.controllers

import it.polito.wa2.g07.loginservice.dtos.ActivationDTO
import it.polito.wa2.g07.loginservice.dtos.UserDTO
import it.polito.wa2.g07.loginservice.utils.*
import it.polito.wa2.g07.loginservice.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
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

    @PostMapping("/admin/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun adminRegistration(@RequestBody body: RegistrationAdmin): AdminRegistrationResponse {
        val superadmin = SecurityContextHolder.getContext().authentication.principal as UserDTO
        try {
            val userDTO =
                service.registerAdmin(
                    UserDTO(
                        username = body.nickname,
                        email = body.email,
                        password = body.password
                    ),
                    superadmin.username,
                    body.role
                )

            return AdminRegistrationResponse(
                provisional_id = userDTO.id!!, email = userDTO.email!!
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

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    fun putMyProfile(@RequestBody body: UpdatePassword){
        try {
            val userDto = SecurityContextHolder.getContext().authentication.principal as UserDTO
            service.updateUserPassword(userDto, body)
        }
        catch(e: Exception){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }


}
