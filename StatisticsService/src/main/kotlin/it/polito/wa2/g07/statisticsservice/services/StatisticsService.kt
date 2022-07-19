package it.polito.wa2.g07.statisticsservice.services

import it.polito.wa2.g07.statisticsservice.dtos.TransactionDTO
import it.polito.wa2.g07.statisticsservice.dtos.TransitDTO

interface StatisticsService {
    fun saveTransaction(transaction: TransactionDTO)
    fun saveTransit(transit: TransitDTO)
}