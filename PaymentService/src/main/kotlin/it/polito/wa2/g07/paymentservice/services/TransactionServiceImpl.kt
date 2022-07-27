package it.polito.wa2.g07.paymentservice.services


import it.polito.wa2.g07.paymentservice.documents.Transaction
import it.polito.wa2.g07.paymentservice.dtos.TransactionDTO
import it.polito.wa2.g07.paymentservice.repositories.TransactionRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TransactionServiceImpl(val transactionRepository: TransactionRepository) : TransactionService  {
  override fun getTransactionsByOwner(owner: Mono<String>): Flux<Transaction> {
    return transactionRepository.findTransactionsByOwner(owner)
  }
  override fun getAllTransactions(): Flux<Transaction> {
    return transactionRepository.findAll()
  }

  override fun addTransaction(transactionDTO: TransactionDTO): Mono<Transaction> {
    return  transactionRepository.save(Transaction(
            owner = transactionDTO.owner,
            totalCost = transactionDTO.totalCost,
            ticketsAmount = transactionDTO.ticketsAmount,
            ticketId = transactionDTO.ticketId
    ))
  }
}