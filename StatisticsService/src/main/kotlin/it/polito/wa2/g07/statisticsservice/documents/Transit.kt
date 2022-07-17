package it.polito.wa2.g07.statisticsservice.documents

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document
data class Transit(
    @Id
    val id: ObjectId = ObjectId.get(),
    val turnstileId: ObjectId,
    val ticketType: ObjectId,
    val timestamp: Date,
){

}