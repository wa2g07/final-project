package it.polito.wa2.g07.travelerservice.services

import it.polito.wa2.g07.travelerservice.dtos.UserDetailsDTO
import it.polito.wa2.g07.travelerservice.dtos.toDTO
import it.polito.wa2.g07.travelerservice.entities.UserDetails
import it.polito.wa2.g07.travelerservice.repositories.TicketPurchasedRepository
import it.polito.wa2.g07.travelerservice.repositories.UserDetailsRepository
import it.polito.wa2.g07.travelerservice.utils.UpdateProfileData
import it.polito.wa2.g07.travelerservice.utils.makeJWT
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import it.polito.wa2.g07.travelerservice.dtos.TicketPurchasedDTO
import it.polito.wa2.g07.travelerservice.entities.TicketPurchased
import org.springframework.beans.factory.annotation.Value
import javax.annotation.PostConstruct
import javax.crypto.SecretKey

@Service
@Transactional
class UserServiceImpl(val userDetailsRepository: UserDetailsRepository, val ticketPurchasedRepository: TicketPurchasedRepository) : UserService {

    @Value("\${secrets.ticketJwtKey}")
    lateinit var b64Key: String

    lateinit var key : SecretKey

    @PostConstruct
    fun initKey(){
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(b64Key))
    }

    override fun getSecret() : String{
        return b64Key
    }
    override fun getUser(userDetailsDTO: UserDetailsDTO): UserDetailsDTO {
        val u = userDetailsRepository.getUserDetailsByUsername(userDetailsDTO.username) ?: return userDetailsDTO
        return u.toDTO()
    }

    override fun updateUser(userDetailsDTO: UserDetailsDTO, update: UpdateProfileData) {
        val u = userDetailsRepository.getUserDetailsByUsername(userDetailsDTO.username)
        if (u != null) {
            if (update.address != null) u.address = update.address
            if (update.name!=null) u.name = update.name
            if (update.telephone != null) u.telephone = update.telephone
            if (update.dateOfBirth != null) u.dateOfBirth = update.dateOfBirth
        }
        else {
            val newUserDetails = UserDetails(
                username = userDetailsDTO.username,
                address = update.address ?: "",
                name = update.name ?: "",
                telephone = update.telephone ?: "",
                dateOfBirth = update.dateOfBirth
            )
            userDetailsRepository.save(newUserDetails)
        }
    }

    override fun getTicketsPurchased(userDetailsDTO: UserDetailsDTO): List<TicketPurchasedDTO> {
        val u = userDetailsRepository.getUserDetailsByUsername(userDetailsDTO.username)
        return u?.tickets?.map { it.toDTO() } ?: emptyList()
    }

    override fun getTicketPurchased(userDetailsDTO: UserDetailsDTO, ticketId: Long): TicketPurchasedDTO {
        val t = ticketPurchasedRepository.findById(ticketId).get()
        if (t.user!!.username != userDetailsDTO.username){
            throw Exception();
        }
        else
            return t.toDTO();
    }

    override fun buyTickets(userDetailsDTO: UserDetailsDTO, quantity: Int, zones: String): List<TicketPurchasedDTO> {
        var u = userDetailsRepository.getUserDetailsByUsername(userDetailsDTO.username)
        if( u == null){
            val newUserDetails = UserDetails(
                username = userDetailsDTO.username,
                address =  "",
                name =  "",
                telephone = "",
                dateOfBirth = null
            )
            u = userDetailsRepository.save(newUserDetails)
        }
        val newTickets = mutableListOf<TicketPurchasedDTO>()
        for (i in 1..quantity){
            val newTicket = ticketPurchasedRepository.save(TicketPurchased(zones = zones,user = u)) //create the ticket entity to retrieve its id
            val jws = makeJWT(sub = newTicket.id.toString(),
                    iat = newTicket.iat,
                    exp = newTicket.exp,
                    zones = newTicket.zones,
                    key = key)
            newTicket.jws = jws
            newTickets.add(newTicket.toDTO())
        }
        return newTickets
    }
}