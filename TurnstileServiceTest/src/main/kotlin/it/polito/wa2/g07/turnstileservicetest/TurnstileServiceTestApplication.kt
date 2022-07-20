package it.polito.wa2.g07.turnstileservicetest

import it.polito.wa2.g07.turnstileservicetest.kafka.TransitInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import java.lang.Thread.sleep
import java.time.LocalDateTime
import java.util.*

@SpringBootApplication
class TurnstileServiceTestApplication {

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, Any>

    @Bean
    fun runner(){
        ApplicationRunner {
            while (true){
                val message: org.springframework.messaging.Message<TransitInfo> = MessageBuilder
                        .withPayload(TransitInfo(
                            turnstileId = 1,
                            ticketId = 3,
                            ticketType = "type4",
                            timestamp = Date(),
                            username = "customer1"
                        )
                        )
                        .setHeader(KafkaHeaders.TOPIC, "outcome")
                        .build()
                val message1: org.springframework.messaging.Message<TransitInfo> = MessageBuilder
                        .withPayload(TransitInfo(
                                turnstileId = 1,
                                ticketId = 2,
                                ticketType = "type2",
                                timestamp = Date(),
                                username = "customer2"
                        )
                        )
                        .setHeader(KafkaHeaders.TOPIC, "outcome")
                        .build()
                kafkaTemplate.send(message)
                kafkaTemplate.send(message1)
                sleep(3000)
            }
        }
    }
    fun main(args: Array<String>) {
        runApplication<TurnstileServiceTestApplication>(*args)
    }

}