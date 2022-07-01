package it.polito.wa2.g07.lab4.services

import it.polito.wa2.g07.lab4.dtos.TicketPurchasedDTO
import it.polito.wa2.g07.lab4.dtos.UserDetailsDTO
import it.polito.wa2.g07.lab4.utils.UpdateProfileData

interface UserService {
    fun getUser(userDetailsDTO: UserDetailsDTO): UserDetailsDTO
    fun updateUser(userDetailsDTO: UserDetailsDTO, update: UpdateProfileData)
    fun getTicketPurchased(userDetailsDTO: UserDetailsDTO) : List<TicketPurchasedDTO>
    fun buyTickets(userDetailsDTO: UserDetailsDTO, quantity: Int, zones: String) : List<TicketPurchasedDTO>
}