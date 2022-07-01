package it.polito.wa2.g07.paymentservice.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Serializer
import org.slf4j.LoggerFactory


class OutcomeSerializer : Serializer<Outcome> {
  private val objectMapper = ObjectMapper()
  private val log = LoggerFactory.getLogger(javaClass)

  override fun serialize(topic: String?, data: Outcome?): ByteArray? {
    log.info("Serializing...")
    return objectMapper.writeValueAsBytes(
      data ?: throw SerializationException("Error when serializing BillingInformation to ByteArray[]")
    )
  }

  override fun close() {}
}