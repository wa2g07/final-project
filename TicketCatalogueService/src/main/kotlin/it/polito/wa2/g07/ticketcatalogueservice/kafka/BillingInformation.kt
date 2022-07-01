package it.polito.wa2.g07.ticketcatalogueservice.kafka

import com.fasterxml.jackson.annotation.JsonProperty

data class BillingInformation(
  @JsonProperty("orderId")
  val orderId: String,
  @JsonProperty("ticketsAmount")
  val ticketsAmount: Int,
  @JsonProperty("ticketId")
  val ticketId: String,
  @JsonProperty("creditCardNumber")
  val creditCardNumber: String,
  @JsonProperty("expDate")
  val expDate: String,
  @JsonProperty("cvv")
  val cvv: String,
  @JsonProperty("owner")
  val owner: String,
  @JsonProperty("totCost")
  val totCost: Double,
  @JsonProperty("jwt")
  val jwt: String)
