package it.polito.wa2.g07.statisticsservice.services

import it.polito.wa2.g07.statisticsservice.documents.Transaction
import it.polito.wa2.g07.statisticsservice.documents.Transit
import it.polito.wa2.g07.statisticsservice.dtos.TransactionDTO
import it.polito.wa2.g07.statisticsservice.dtos.TransitCountDTO
import it.polito.wa2.g07.statisticsservice.dtos.TransitDTO
import it.polito.wa2.g07.statisticsservice.repositories.TransactionRepository
import it.polito.wa2.g07.statisticsservice.repositories.TransitRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.*

@Service
class StatisticsServiceImpl(val transactionRepository: TransactionRepository,
                            val transitRepository: TransitRepository
): StatisticsService {
    override fun saveTransaction(transactionDTO: TransactionDTO) {
        transactionRepository.save(Transaction(
            success = transactionDTO.success,
            ticketAmount = transactionDTO.ticketAmount,
            ticketId = transactionDTO.ticketId,
            cost = transactionDTO.cost,
            date = transactionDTO.date,
            username = transactionDTO.username)
        ).block()
    }

    override fun saveTransit(transitDTO: TransitDTO) {
        transitRepository.save(Transit(
            turnstileId = transitDTO.turnstileId,
            ticketId = transitDTO.ticketId,
            ticketType = transitDTO.ticketType,
            timestamp = transitDTO.timestamp,
            username = transitDTO.username)
        ).block()
    }

    override fun getTransitCountPerDay(from: Date, to: Date): Flux<TransitCountDTO> {
        val today = Date()
        if(to.before(from))
            throw Exception("To param cannot be past with respect to before param")
        if(from.month == today.month && from.year == today.year && from.date > today.date)
            throw Exception("From param cannot be in the future with respect to today")
        if(to.month == today.month && to.year == today.year && to.date > today.date)
            throw Exception("To param cannot be in the future with respect to today")
        return transitRepository.getTransitsCountPerDay(from, to)
        //return Flux.empty()
    }

    override fun getTransitCountPerHour(day: Date): Flux<TransitCountDTO> {
        val today = Date()
        if(day.month == today.month && day.year == today.year && day.date > today.date)
            throw Exception("Day param cannot be in the future with respect to today")
        return transitRepository.getTransitsCountPerHour(day)
        //return Flux.empty()
    }

    override fun getMyTransitCountPerHour(from: Date, to: Date, username: String): Flux<TransitCountDTO> {
        val today = Date()
        if(to.before(from))
            throw Exception("To param cannot be past with respect to before param")
        if(from.month == today.month && from.year == today.year && from.date > today.date)
            throw Exception("From param cannot be in the future with respect to today")
        if(to.month == today.month && to.year == today.year && to.date > today.date)
            throw Exception("To param cannot be in the future with respect to today")
        return transitRepository.getMyTransitsCountPerHour(from, to, username)
        //return Flux.empty()
    }
}