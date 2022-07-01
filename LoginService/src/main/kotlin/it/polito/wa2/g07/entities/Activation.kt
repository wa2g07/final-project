package it.polito.wa2.g07.entities

import it.polito.wa2.g07.utils.generateActivationCode
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.Reference
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "activations")
class Activation(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy="uuid2")
    var id : UUID?=null,
    @OneToOne
    var user: User?=null,
    var activationCode: String = generateActivationCode(),
    @Temporal(value=TemporalType.TIMESTAMP)

    //this one is for a real use case and for RegistrationControllerTests

    var activationDeadline: Date = Date(
            LocalDateTime.now().plusMinutes(10).
            atZone(ZoneId.systemDefault()).toInstant()
                    .toEpochMilli()
    ),

    /*
    //this second is used for testing. In order to not make the test sleep for 10 minutes. To use for ActivateUserTests.
    var activationDeadline: Date = Date(
            LocalDateTime.now().plusSeconds(20).
            atZone(ZoneId.systemDefault()).toInstant()
                    .toEpochMilli()
    ),
    */
    var attemptsCounter:Int = 5

): EntityBase<UUID>(){
    fun attachUser(user: User){
        this.user = user
        user.activation = this
    }

    fun detachUser(){
        this.user?.activation = null
        this.user = null
    }
}