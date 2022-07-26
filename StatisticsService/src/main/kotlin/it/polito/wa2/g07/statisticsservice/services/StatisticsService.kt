package it.polito.wa2.g07.statisticsservice.services

import it.polito.wa2.g07.statisticsservice.dtos.DoubleCountDTO
import it.polito.wa2.g07.statisticsservice.dtos.TransactionDTO
import it.polito.wa2.g07.statisticsservice.dtos.LongCountDTO
import it.polito.wa2.g07.statisticsservice.dtos.TransitDTO
import reactor.core.publisher.Flux
import java.util.Date

interface StatisticsService {
    fun saveTransaction(transactionDTO: TransactionDTO)
    fun saveTransit(transitDTO: TransitDTO)
    fun getTransitCountPerDay(from_string: String, to_string: String): Flux<LongCountDTO>
    fun getTransitCountPerHour(day: Date): Flux<LongCountDTO>
    fun getMyTransitCountPerHour(from: Date, to: Date, username: String): Flux<LongCountDTO>

    fun getRevenuesPerMonth(year: Int): Flux<DoubleCountDTO>

    fun getMyExpensesPerMonth(year: Int, username: String): Flux<DoubleCountDTO>

    fun getTopBuyers(limit: Int, year: Int): Flux<LongCountDTO>

}