package it.polito.wa2.g07.travelerservice.entities

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tickets_purchased")
class TicketPurchased(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @Temporal(value = TemporalType.TIMESTAMP)
  var iat: Date = Date(
    LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
  ),

  @Temporal(value = TemporalType.TIMESTAMP)
  var exp: Date = Date(
    LocalDateTime.now()
      .plusMinutes(60).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
  ),

  var zones: String,

  @Lob
  var jws: String = "",

  @ManyToOne
  var user: UserDetails? = null
) : EntityBase<Long>()