package it.polito.wa2.g07.paymentservice.kafka

import it.polito.wa2.g07.paymentservice.dtos.TransactionDTO
import it.polito.wa2.g07.paymentservice.services.TransactionService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Component
class Consumer {
  private val logger = LoggerFactory.getLogger(javaClass)

  @Autowired
  lateinit var kafkaTemplate: KafkaTemplate<String, Any>

  @Autowired
  lateinit var transactionService: TransactionService
  @KafkaListener(topics = ["\${kafka.topics.order}"], groupId = "ppr")
  fun listener(consumerRecord: ConsumerRecord<Any, Any>, ack: Acknowledgment) {
    logger.info("Message received {}", consumerRecord)
    ack.acknowledge()
    val billingInfo = consumerRecord.value() as BillingInformation
    val out = (0..100).random()
    if(out < 70) {
      val t = transactionService.addTransaction(TransactionDTO(
              owner = billingInfo.owner,
              totalCost = billingInfo.totCost,
              ticketsAmount = billingInfo.ticketsAmount,
              ticketId = billingInfo.ticketId
      )).block()
      val message: org.springframework.messaging.Message<Outcome> = MessageBuilder
          .withPayload(Outcome(
                  result = "success",
                  orderId = billingInfo.orderId,
                  ticketsAmount = billingInfo.ticketsAmount,
                  ticketId = billingInfo.ticketId,
                  creditCardNumber = billingInfo.creditCardNumber,
                  expDate = billingInfo.creditCardNumber,
                  cvv = billingInfo.cvv,
                  owner = billingInfo.owner,
                  totCost = billingInfo.totCost,
                  jwt = billingInfo.jwt,
                  date = Date())
          )
          .setHeader(KafkaHeaders.TOPIC, "outcome")
          .build()
      kafkaTemplate.send(message)
    }
    else {
      val message: org.springframework.messaging.Message<Outcome> = MessageBuilder
          .withPayload(Outcome(
              result = "failure",
              orderId = billingInfo.orderId,
              ticketsAmount = billingInfo.ticketsAmount,
              ticketId = billingInfo.ticketId,
              creditCardNumber = billingInfo.creditCardNumber,
              expDate = billingInfo.creditCardNumber,
              cvv = billingInfo.cvv,
              owner = billingInfo.owner,
              totCost = billingInfo.totCost,
              jwt = billingInfo.jwt,
              date = Date())
          )
          .setHeader(KafkaHeaders.TOPIC, "outcome")
          .build()
      kafkaTemplate.send(message)
    }
  }
}