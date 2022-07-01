package it.polito.wa2.g07.travelerservice.repositories

import it.polito.wa2.g07.travelerservice.entities.TicketPurchased
import org.springframework.data.repository.CrudRepository

interface TicketPurchasedRepository : CrudRepository<TicketPurchased, Long>