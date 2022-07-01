package it.polito.wa2.g07.ticketcatalogueservice.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.LoggerFactory
import kotlin.text.Charsets.UTF_8


class OutcomeDeserializer : Deserializer<Outcome> {
  private val objectMapper = ObjectMapper()
  private val log = LoggerFactory.getLogger(javaClass)

  override fun deserialize(topic: String?, data: ByteArray?): Outcome? {
    log.info("Deserializing...")
    return objectMapper.readValue(
      String(
        data ?: throw SerializationException("Error when deserializing byte[] to BillingInformation"), UTF_8
      ), Outcome::class.java
    )
  }

  override fun close() {}

}