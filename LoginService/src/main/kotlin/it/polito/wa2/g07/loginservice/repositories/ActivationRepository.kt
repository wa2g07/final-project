package it.polito.wa2.g07.loginservice.repositories

import it.polito.wa2.g07.loginservice.entities.Activation
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ActivationRepository: CrudRepository<Activation, UUID> {
  @Query("select a from Activation a where a.activationDeadline < ?1")
  fun getActivationByActivationDeadlineBeforeDate(currentDate: Date): MutableList<Activation>
}
