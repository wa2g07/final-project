package it.polito.wa2.g07.statisticsservice.repositories

import it.polito.wa2.g07.statisticsservice.documents.Transit
import it.polito.wa2.g07.statisticsservice.dtos.TransitCountDTO
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.Date

@Repository
interface TransitRepository : ReactiveMongoRepository<Transit, ObjectId> {
    /*
    @Query()
    fun getTransitsCountPerDay(from: Date, to: Date): Flux<TransitCountDTO>
    @Query()
    fun getTransitsCountPerHour(day: Date): Flux<TransitCountDTO>
    @Query()
    fun getMyTransitsCountPerHour(from: Date, to: Date, username: String): Flux<TransitCountDTO>
    */
}
