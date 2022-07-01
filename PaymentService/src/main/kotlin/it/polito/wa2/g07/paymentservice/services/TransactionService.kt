package it.polito.wa2.g07.paymentservice.services

import it.polito.wa2.g07.paymentservice.documents.Transaction
import it.polito.wa2.g07.paymentservice.dtos.TransactionDTO
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


interface TransactionService {

  fun getTransactionsByOwner(owner: Mono<String>): Flux<Transaction>
  fun getAllTransactions(): Flux<Transaction>
  fun addTransaction(transactionDTO: TransactionDTO): Mono<Transaction>
}