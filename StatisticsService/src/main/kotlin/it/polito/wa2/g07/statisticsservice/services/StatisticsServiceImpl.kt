package it.polito.wa2.g07.statisticsservice.services

import it.polito.wa2.g07.statisticsservice.documents.Transaction
import it.polito.wa2.g07.statisticsservice.documents.Transit
import it.polito.wa2.g07.statisticsservice.dtos.DoubleCountDTO
import it.polito.wa2.g07.statisticsservice.dtos.TransactionDTO
import it.polito.wa2.g07.statisticsservice.dtos.LongCountDTO
import it.polito.wa2.g07.statisticsservice.dtos.TransitDTO
import it.polito.wa2.g07.statisticsservice.repositories.TransactionRepository
import it.polito.wa2.g07.statisticsservice.repositories.TransitRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.text.SimpleDateFormat
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
            timestamp = transactionDTO.date,
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

    override fun getTransitCountPerDay(from_string: String, to_string: String): Flux<LongCountDTO> {
        val today = Date()
        val from = SimpleDateFormat("yyyyMMdd").parse(from_string)
        val to = SimpleDateFormat("yyyyMMdd").parse(to_string)
        if(to.before(from))
            throw Exception("To param cannot be past with respect to before param")
        if(from.month == today.month && from.year == today.year && from.date > today.date)
            throw Exception("From param cannot be in the future with respect to today")
        if(to.month == today.month && to.year == today.year && to.date > today.date)
            throw Exception("To param cannot be in the future with respect to today")
        //val res= transitRepository.getTransitsCountPerDay(from, to).asFlow()
        return transitRepository.getTransitsCountPerDay(from_string, to_string)
    }

    override fun getTransitCountPerHour(day: Date): Flux<LongCountDTO> {
        val today = Date()
        if(day.month == today.month && day.year == today.year && day.date > today.date)
            throw Exception("Day param cannot be in the future with respect to today")
        return transitRepository.getTransitsCountPerHour(day)
    }

    override fun getMyTransitCountPerHour(from: Date, to: Date, username: String): Flux<LongCountDTO> {
        val today = Date()
        if(to.before(from))
            throw Exception("To param cannot be past with respect to before param")
        if(from.month == today.month && from.year == today.year && from.date > today.date)
            throw Exception("From param cannot be in the future with respect to today")
        if(to.month == today.month && to.year == today.year && to.date > today.date)
            throw Exception("To param cannot be in the future with respect to today")
        return transitRepository.getMyTransitsCountPerHour(from, to, username)
    }

    override fun getRevenuesPerMonth(year: Int): Flux<DoubleCountDTO> {
        val today = Date()
        if(today.year < year)
            throw Exception("Year param cannot be in the future")
        return transactionRepository.getRevenuesPerMonth(year)
    }

    override fun getMyExpensesPerMonth(year: Int, username: String): Flux<DoubleCountDTO> {
        val today = Date()
        if(today.year < year)
            throw Exception("Year param cannot be in the future")
        return transactionRepository.getMyExpensesPerMonth(year, username)
    }

    override fun getTopBuyers(limit: Int, year: Int): Flux<LongCountDTO> {
        val today = Date()
        if(today.year < year)
            throw Exception("Year param cannot be in the future")
        return transactionRepository.getTopBuyers(limit, year)
    }

}