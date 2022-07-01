package it.polito.wa2.g07.travelerservice.entities

import java.util.*
import javax.persistence.*


@Entity
@Table(name = "users_details")
class UserDetails(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id : Long? = null,
    var username: String = "",
    var name : String = "",
    var address : String = "",
    var telephone : String = "",

    @Temporal(TemporalType.DATE)
    var dateOfBirth : Date? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    var tickets: MutableList<TicketPurchased> = mutableListOf()
) : EntityBase<Long>()