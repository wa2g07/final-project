package it.polito.wa2.g07.ticketcatalogueservice.kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaConfig(
  @Value("\${kafka.bootstrapAddress}")
  private val servers: String,
  @Value("\${kafka.topics.order}")
  private val topic: String
) {

  @Bean
  fun kafkaAdmin(): KafkaAdmin {
    val configs: MutableMap<String, Any?> = HashMap()
    configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
    return KafkaAdmin(configs)
  }

  @Bean
  fun billingInformation(): NewTopic {
    return NewTopic(topic, 1, 1.toShort())
  }

  @Bean
  fun outcomeInformation(): NewTopic {
    return NewTopic("outcome", 1, 1.toShort())
  }
    @Bean
    fun transitInformation(): NewTopic {
      return NewTopic("transit",1,1.toShort())
  }
}