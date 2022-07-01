package it.polito.wa2.g07.servicesTests.userServicesTests

import it.polito.wa2.g07.dtos.ActivationDTO
import it.polito.wa2.g07.dtos.UserDTO
import it.polito.wa2.g07.repositories.ActivationRepository
import it.polito.wa2.g07.repositories.UserRepository
import it.polito.wa2.g07.services.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Assertions.assertTrue
import java.lang.Thread.sleep

import java.util.*

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivateUserTests {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var activationRepository: ActivationRepository

    lateinit var aDTO: ActivationDTO

    val uDTO = UserDTO(username = "user",
            password = "aaBB99!!",
            email = "a@b.com")
    @BeforeEach
    fun clearRepositoriesAndInitialize(){
        userRepository.deleteAll()
        activationRepository.deleteAll()
        aDTO = userService.registerUser(uDTO)
    }

    @Test
    fun invalidActivationIdTest(){

        assertThrows<IllegalArgumentException> {
            userService.validateActivationRequest(ActivationDTO(
                    id="${aDTO.id}asd",
                activationCode = aDTO.activationCode))
        }

        assertThrows<IllegalArgumentException> {
            userService.validateActivationRequest(ActivationDTO(
                    id=UUID.randomUUID().toString(),
                    activationCode = aDTO.activationCode))
        }
    }

    @Test
    fun invalidActivationCodeTest(){
        val a = activationRepository.
        findById(UUID.fromString(aDTO.id)).get()

        assertTrue(userService.validateActivationRequest(ActivationDTO(
                id=aDTO.id,
                activationCode = "${aDTO.activationCode}asd")) == null)
        val a2 = activationRepository.
        findById(UUID.fromString(aDTO.id)).get()

        assertTrue(a.attemptsCounter == a2.attemptsCounter+1)
    }

    @Test
    fun attemptsCounterGoesToZeroTest(){

        assertTrue(activationRepository.
        findById(UUID.fromString(aDTO.id)).isPresent)

        val a = activationRepository.
        findById(UUID.fromString(aDTO.id)).get()

        assertTrue(userRepository.findById(a.user!!.id!!).isPresent)

        for (i in 1..5) {
            assertTrue(userService.validateActivationRequest(ActivationDTO(
                    id=aDTO.id,
                    activationCode = "${aDTO.activationCode}asd")) == null)
        }

        assertTrue(!activationRepository.
        findById(UUID.fromString(aDTO.id)).isPresent)
        assertTrue(!userRepository.findById(a.user!!.id!!).isPresent)

    }

    @Test
    //this test may fail due to timing reasons, see readme.md
    fun deadlineExpiredTest(){
        val secToSleep: Long = 30
        assertTrue(userService.validateActivationRequest(ActivationDTO(
                id=aDTO.id,
                activationCode = "${aDTO.activationCode}asd")) == null)

        assertTrue(activationRepository.
        findById(UUID.fromString(aDTO.id)).isPresent)

        val a = activationRepository.
        findById(UUID.fromString(aDTO.id)).get()

        assertTrue(userRepository.findById(a.user!!.id!!).isPresent)

        sleep(secToSleep*1000)
        assertThrows<IllegalArgumentException> {
            userService.validateActivationRequest(ActivationDTO(
                    id=aDTO.id,
                    activationCode = "${aDTO.activationCode}asd")) }

        assertTrue(!activationRepository.
        findById(UUID.fromString(aDTO.id)).isPresent)
        assertTrue(!userRepository.findById(a.user!!.id!!).isPresent)

    }

    @Test
    fun activateUserSuccessfullyTest(){

        assertTrue {
            val u = activationRepository.findById(
                    UUID.fromString(aDTO.id)).get().user
            !u!!.activated

        }
        val returnedUserDTO = userService.validateActivationRequest(ActivationDTO(
                id=aDTO.id,
                activationCode = aDTO.activationCode))

        assertTrue(uDTO.username == returnedUserDTO!!.username
                && uDTO.email == returnedUserDTO.email)

        assertTrue {
            val u = userRepository.findById(
                    returnedUserDTO.id!!).get()
            u.activated
        }

        assertTrue {
            !activationRepository.findById(
                    UUID.fromString(aDTO.id)).isPresent
        }
    }
}