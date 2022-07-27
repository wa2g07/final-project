package it.polito.wa2.g07.turnstileservice.controllers

import it.polito.wa2.g07.turnstileservice.kafka.TransitInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
class TransitsController(@Autowired private val kafkaTemplate: KafkaTemplate<String, Any>) {

    @PostMapping("/transits")
    @ResponseStatus(HttpStatus.CREATED)
    fun postTransit(@RequestBody body : TransitInfo) {
        try {
            val message: org.springframework.messaging.Message<TransitInfo> = MessageBuilder
                    .withPayload(body).setHeader(KafkaHeaders.TOPIC, "transit")
                    .build()
            kafkaTemplate.send(message)
        }
        catch (e: Exception){
            print(e)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }

}