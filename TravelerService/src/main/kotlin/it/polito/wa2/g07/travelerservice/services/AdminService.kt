package it.polito.wa2.g07.travelerservice.services

import it.polito.wa2.g07.travelerservice.dtos.TicketPurchasedDTO
import it.polito.wa2.g07.travelerservice.dtos.UserDetailsDTO

interface AdminService {
    fun getAllTravelers() : List<String>
    fun getProfileByUserId(id: Long): UserDetailsDTO
    fun getTicketsByUserId(id: Long) : List<TicketPurchasedDTO>
}