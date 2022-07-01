package it.polito.wa2.g07.loginservice.repositories

import it.polito.wa2.g07.loginservice.entities.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<User,Long> {
    fun existsUserByEmail(email: String) : Boolean
    fun existsUserByUsername(username: String) : Boolean
    fun getUserByUsername(username: String) : User
}
