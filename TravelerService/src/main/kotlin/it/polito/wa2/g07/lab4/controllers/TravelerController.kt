package it.polito.wa2.g07.lab4.controllers

import it.polito.wa2.g07.lab4.dtos.UserDetailsDTO
import it.polito.wa2.g07.lab4.services.AdminService
import it.polito.wa2.g07.lab4.services.UserService
import it.polito.wa2.g07.lab4.utils.BuyTicketRequest
import it.polito.wa2.g07.lab4.utils.ProfileResponse
import it.polito.wa2.g07.lab4.utils.TicketResponse
import it.polito.wa2.g07.lab4.utils.UpdateProfileData
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
class TravelerController(val userService: UserService, val adminService: AdminService ) {

    @GetMapping("/my/profile")
    @ResponseStatus(HttpStatus.OK)
    fun getMyProfile() : ProfileResponse {
        try {
            val userDetailsWithUsernameOnly = SecurityContextHolder.getContext().authentication.principal as UserDetailsDTO
            val userDetailsDTO = userService.getUser(userDetailsWithUsernameOnly)
            return ProfileResponse(
                username = userDetailsDTO.username,
                name = userDetailsDTO.name,
                address = userDetailsDTO.address,
                telephone = userDetailsDTO.telephone,
                dateOfBirth = userDetailsDTO.dateOfBirth
            )
        }
        catch(e: Exception){
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/my/profile")
    @ResponseStatus(HttpStatus.OK)
    fun putMyProfile(@RequestBody body: UpdateProfileData){
        try {
            val userDetailsWithUsernameOnly = SecurityContextHolder.getContext().authentication.principal as UserDetailsDTO
            userService.updateUser(userDetailsWithUsernameOnly, body)
        }
        catch(e: Exception){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/my/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getMyTickets() : List<TicketResponse> {
        try{
            val userDetailsWithUsernameOnly = SecurityContextHolder.getContext().authentication.principal as UserDetailsDTO
            return userService.getTicketPurchased(userDetailsWithUsernameOnly).
            map { TicketResponse(sub = it.id!!,
                    zId = it.zId,
                    iat = it.iat,
                    exp = it.exp,
                    jws = it.jws)}
        }
        catch(e: Exception){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/my/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    fun postMyTickets(@RequestBody body : BuyTicketRequest) : List<TicketResponse> {
        try {
            val userDetailsWithUsernameOnly =
                SecurityContextHolder.getContext().authentication.principal as UserDetailsDTO
            if(body.cmd!!.compareTo("buy_tickets") == 0 && body.quantity!! > 0 && !body.zones.isNullOrBlank()){
                return userService.buyTickets(userDetailsWithUsernameOnly,
                        body.quantity,
                        body.zones).
                map { TicketResponse(sub = it.id!!,
                zId = it.zId,
                iat = it.iat,
                exp = it.exp,
                jws = it.jws)}
            }
            else
                throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
        catch (e: Exception){
            print(e)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/admin/travelers")
    @ResponseStatus(HttpStatus.OK)
    fun getTravelers() : List<String> {
        try{
            return adminService.getAllTravelers()
        }
        catch (e: Exception){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/admin/traveler/{userID}/profile")
    @ResponseStatus(HttpStatus.OK)
    fun getProfileByUserId(@PathVariable userID : Long) : ProfileResponse {
        try{
            val userDetailsDTO = adminService.getProfileByUserId(userID)
            return ProfileResponse(
                username = userDetailsDTO.username,
                name = userDetailsDTO.name,
                address = userDetailsDTO.address,
                telephone = userDetailsDTO.telephone,
                dateOfBirth = userDetailsDTO.dateOfBirth
            )
        }
        catch(e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        catch(e: Exception){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/admin/traveler/{userID}/tickets")
    @ResponseStatus(HttpStatus.OK)
    fun getTicketsByUserId(@PathVariable userID : Long) : List<TicketResponse> {
        try{
            return adminService.getTicketsByUserId(userID).
            map { TicketResponse(sub = it.id!!,
                    zId = it.zId,
                    iat = it.iat,
                    exp = it.exp,
                    jws = it.jws)}
        }
        catch(e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        catch(e: Exception){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }
}