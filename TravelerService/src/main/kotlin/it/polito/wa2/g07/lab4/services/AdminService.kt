package it.polito.wa2.g07.lab4.services

import it.polito.wa2.g07.lab4.dtos.TicketPurchasedDTO
import it.polito.wa2.g07.lab4.dtos.UserDetailsDTO

interface AdminService {
    fun getAllTravelers() : List<String>
    fun getProfileByUserId(id: Long): UserDetailsDTO
    fun getTicketsByUserId(id: Long) : List<TicketPurchasedDTO>
}