package it.polito.wa2.g07.travelerservice.services

import it.polito.wa2.g07.travelerservice.dtos.TicketPurchasedDTO
import it.polito.wa2.g07.travelerservice.dtos.UserDetailsDTO
import it.polito.wa2.g07.travelerservice.utils.UpdateProfileData

interface UserService {
    fun getUser(userDetailsDTO: UserDetailsDTO): UserDetailsDTO
    fun updateUser(userDetailsDTO: UserDetailsDTO, update: UpdateProfileData)
    fun getTicketsPurchased(userDetailsDTO: UserDetailsDTO) : List<TicketPurchasedDTO>
    fun getTicketPurchased(userDetailsDTO: UserDetailsDTO, ticketId: Long) : TicketPurchasedDTO
    fun buyTickets(userDetailsDTO: UserDetailsDTO, quantity: Int, zones: String) : List<TicketPurchasedDTO>
    fun getSecret() : String
}