package it.polito.wa2.g07.statisticsservice.services

import it.polito.wa2.g07.statisticsservice.dtos.TransactionDTO
import it.polito.wa2.g07.statisticsservice.dtos.TransitCountDTO
import it.polito.wa2.g07.statisticsservice.dtos.TransitDTO
import reactor.core.publisher.Flux
import java.util.Date

interface StatisticsService {
    fun saveTransaction(transactionDTO: TransactionDTO)
    fun saveTransit(transitDTO: TransitDTO)
    fun getTransitCountPerDay(from: Date, to: Date): Flux<TransitCountDTO>
    fun getTransitCountPerHour(day: Date): Flux<TransitCountDTO>
    fun getMyTransitCountPerHour(from: Date, to: Date, username: String): Flux<TransitCountDTO>
}