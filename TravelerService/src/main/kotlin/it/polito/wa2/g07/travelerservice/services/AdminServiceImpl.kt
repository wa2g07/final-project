package it.polito.wa2.g07.travelerservice.services

import it.polito.wa2.g07.travelerservice.dtos.TicketPurchasedDTO
import it.polito.wa2.g07.travelerservice.dtos.UserDetailsDTO
import it.polito.wa2.g07.travelerservice.dtos.toDTO
import it.polito.wa2.g07.travelerservice.repositories.UserDetailsRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class AdminServiceImpl(
  val userDetailsRepository: UserDetailsRepository
) : AdminService {

  override fun getAllTravelers(): List<String> {
    return userDetailsRepository.findAll().map { it.username }
  }

  override fun getProfileByUserId(id: Long): UserDetailsDTO {
    return userDetailsRepository.findById(id).get().toDTO()
  }

  override fun getTicketsByUserId(id: Long): List<TicketPurchasedDTO> {
    return userDetailsRepository.findById(id).get().tickets.map { it.toDTO() }
  }
}