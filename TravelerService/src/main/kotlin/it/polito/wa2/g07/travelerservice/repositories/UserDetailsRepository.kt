package it.polito.wa2.g07.travelerservice.repositories

import it.polito.wa2.g07.travelerservice.entities.UserDetails
import org.springframework.data.repository.CrudRepository

interface UserDetailsRepository : CrudRepository<UserDetails, Long> {
    fun getUserDetailsByUsername(username: String): UserDetails?
}