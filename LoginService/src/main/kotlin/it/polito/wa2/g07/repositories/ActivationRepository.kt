package it.polito.wa2.g07.repositories

import it.polito.wa2.g07.entities.Activation
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ActivationRepository: CrudRepository<Activation, UUID> {
  @Query("select a from Activation a where a.activationDeadline < ?1")
  fun getActivationByActivationDeadlineBeforeDate(currentDate: Date): MutableList<Activation>
}
