package it.polito.wa2.g07.lab4.repositories

import it.polito.wa2.g07.lab4.entities.TicketPurchased
import org.springframework.data.repository.CrudRepository

interface TicketPurchasedRepository : CrudRepository<TicketPurchased, Long>