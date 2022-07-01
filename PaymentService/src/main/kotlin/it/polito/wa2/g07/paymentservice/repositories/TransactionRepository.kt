package it.polito.wa2.g07.paymentservice.repositories

import it.polito.wa2.g07.paymentservice.documents.Transaction
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface TransactionRepository : ReactiveMongoRepository<Transaction, ObjectId>{
    fun findTransactionsByOwner(owner: Mono<String>): Flux<Transaction>
}
