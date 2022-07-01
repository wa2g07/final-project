package it.polito.wa2.g07.lab4.repositories

import it.polito.wa2.g07.lab4.entities.UserDetails
import org.springframework.data.repository.CrudRepository

interface UserDetailsRepository : CrudRepository<UserDetails, Long> {
    fun getUserDetailsByUsername(username: String): UserDetails?
}