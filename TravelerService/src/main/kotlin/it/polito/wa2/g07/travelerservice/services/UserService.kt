package it.polito.wa2.g07.travelerservice.services

import it.polito.wa2.g07.travelerservice.dtos.TicketPurchasedDTO
import it.polito.wa2.g07.travelerservice.dtos.UserDetailsDTO
import it.polito.wa2.g07.travelerservice.utils.UpdateProfileData

interface UserService {
    fun getUser(userDetailsDTO: UserDetailsDTO): UserDetailsDTO
    fun updateUser(userDetailsDTO: UserDetailsDTO, update: UpdateProfileData)
    fun getTicketPurchased(userDetailsDTO: UserDetailsDTO) : List<TicketPurchasedDTO>
    fun buyTickets(userDetailsDTO: UserDetailsDTO, quantity: Int, zones: String) : List<TicketPurchasedDTO>
}