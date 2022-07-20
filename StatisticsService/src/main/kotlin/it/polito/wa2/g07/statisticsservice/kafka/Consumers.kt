package it.polito.wa2.g07.statisticsservice.kafka

import it.polito.wa2.g07.statisticsservice.dtos.TransactionDTO
import it.polito.wa2.g07.statisticsservice.dtos.TransitDTO
import it.polito.wa2.g07.statisticsservice.services.StatisticsService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class Consumers {
    private val logger = LoggerFactory.getLogger(javaClass)

//    @Autowired
//    lateinit var kafkaTemplate: KafkaTemplate<String, TransitInfo>

    @Autowired
    lateinit var statisticsService: StatisticsService

    @KafkaListener(topics = ["transit"], groupId = "ppr4", containerFactory = "transitInfokafkaListenerContainerFactory")
    fun transitListener(consumerRecord: ConsumerRecord<Any, Any>, ack: Acknowledgment) {
        logger.info("Message received {}", consumerRecord)
        ack.acknowledge()
        val transitInfo = consumerRecord.value() as TransitInfo
        statisticsService.saveTransit(TransitDTO(
            turnstileId = transitInfo.turnstileId,
            ticketId = transitInfo.ticketId,
            ticketType = ObjectId(transitInfo.ticketType),
            timestamp = transitInfo.timestamp,
            username = transitInfo.username)
        )
    }

    @KafkaListener(topics = ["outcome"], groupId = "ppr3", containerFactory = "TransactionInfokafkaListenerContainerFactory")
    fun transactionListener(consumerRecord: ConsumerRecord<Any, Any>, ack: Acknowledgment) {
        logger.info("Message received {}", consumerRecord)
        ack.acknowledge()
        val transactionInfo = consumerRecord.value() as TransactionInfo
        var success : Boolean = true
        if(transactionInfo.result.compareTo("failure")==0)
            success = false
        statisticsService.saveTransaction(TransactionDTO(
            success = success,
            ticketAmount = transactionInfo.ticketsAmount,
            ticketId =  ObjectId(transactionInfo.ticketId),
            cost = transactionInfo.totCost,
            date = transactionInfo.date,
            username = transactionInfo.owner)
        )
    }
}