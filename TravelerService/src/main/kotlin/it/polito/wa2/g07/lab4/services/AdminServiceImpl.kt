package it.polito.wa2.g07.lab4.services

import it.polito.wa2.g07.lab4.dtos.TicketPurchasedDTO
import it.polito.wa2.g07.lab4.dtos.UserDetailsDTO
import it.polito.wa2.g07.lab4.dtos.toDTO
import it.polito.wa2.g07.lab4.repositories.UserDetailsRepository
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