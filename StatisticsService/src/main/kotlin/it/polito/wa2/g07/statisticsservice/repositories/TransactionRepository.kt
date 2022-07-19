package it.polito.wa2.g07.statisticsservice.repositories

import it.polito.wa2.g07.statisticsservice.documents.Transaction
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : ReactiveMongoRepository<Transaction, ObjectId> {
}