package it.polito.wa2.g07.dtosTests

import it.polito.wa2.g07.dtos.toDTO
import it.polito.wa2.g07.entities.Activation
import it.polito.wa2.g07.entities.User
import it.polito.wa2.g07.repositories.ActivationRepository
import it.polito.wa2.g07.repositories.UserRepository
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DtosTests {

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var activationRepository: ActivationRepository

    var u: User? = null
    var a: Activation? = null

    @BeforeAll
    internal fun init(){
        u = userRepository.save(User(username="user-test",password="pass-test"))
        a = Activation()
        a!!.attachUser(u as User)
        a = activationRepository.save(a as Activation)
    }

    @Test
    fun userDTOTest(){
        assertDoesNotThrow { val dto = u!!.toDTO()
        println(dto)
        }
    }

    @Test
    fun activationDTOTest(){
        assertDoesNotThrow { val dto = a!!.toDTO()
        println(dto)}

    }

    @Test
    fun matchBetweenDTOTest(){
        val userDTO = u!!.toDTO()
        val activationDTO = a!!.toDTO()
        assert(userDTO.id == activationDTO.userId)
        assert(userDTO.activationId == activationDTO.id)
    }
}