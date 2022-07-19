package it.polito.wa2.g07.statisticsservice.services

import it.polito.wa2.g07.statisticsservice.documents.Transaction
import it.polito.wa2.g07.statisticsservice.documents.Transit
import it.polito.wa2.g07.statisticsservice.dtos.TransactionDTO
import it.polito.wa2.g07.statisticsservice.dtos.TransitDTO
import it.polito.wa2.g07.statisticsservice.repositories.TransactionRepository
import it.polito.wa2.g07.statisticsservice.repositories.TransitRepository
import org.springframework.stereotype.Service

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
        )
    }

    override fun saveTransit(transitDTO: TransitDTO) {
        transitRepository.save(Transit(
            turnstileId = transitDTO.turnstileId,
            ticketId = transitDTO.ticketId,
            ticketType = transitDTO.ticketType,
            timestamp = transitDTO.timestamp,
            username = transitDTO.username)
        )
    }
}