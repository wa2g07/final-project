package it.polito.wa2.g07.ticketcatalogueservice.documents

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Ticket(
  @Id val id: ObjectId = ObjectId.get(),
  val price: Float,
  val type: String,

  //two numbers followed by the 'p' o 'm' char
  //'p' stands for 'plus' and 'm' stands for 'minus'
  //60p means that the ticket can be bought only by people with age >= 60
  //09m means that the ticket can be bought only by people with age <= 09
  //if null, no age restrictions are applied
  val ageRestriction: String?,
  val zones: String
  )
