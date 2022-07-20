package it.polito.wa2.g07.turnstileservice

import it.polito.wa2.g07.turnstileservice.kafka.TransitInfo
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
class TurnstileServiceApplication {

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String,Any?>

    @Bean
    fun runner() = ApplicationRunner{
        val message1: org.springframework.messaging.Message<TransitInfo> = MessageBuilder
                .withPayload(TransitInfo(
                        turnstileId = 1,
                        ticketId = 123455909,
                        ticketType = "62d7cc2bfefe406f7f95855c",
                        timestamp = Date(),
                        username = "customer"

                )).setHeader(KafkaHeaders.TOPIC, "transit")
                .build()
        val message2: org.springframework.messaging.Message<TransitInfo> = MessageBuilder
                .withPayload(TransitInfo(
                        turnstileId = 1,
                        ticketId = 123455907,
                        ticketType = "62d7cc2bfefe406f7f95855c",
                        timestamp = Date(),
                        username = "customer"

                )).setHeader(KafkaHeaders.TOPIC, "transit")
                .build()
        while(true){
            kafkaTemplate.send(message1)
            sleep(3000)
            kafkaTemplate.send(message2)
            sleep(3000)
        }
    }
}
fun main(args: Array<String>) {
    runApplication<TurnstileServiceApplication>(*args)
}
