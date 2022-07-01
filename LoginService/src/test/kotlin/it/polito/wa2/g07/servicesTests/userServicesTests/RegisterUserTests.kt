package it.polito.wa2.g07.servicesTests.userServicesTests


import it.polito.wa2.g07.dtos.UserDTO
import it.polito.wa2.g07.repositories.ActivationRepository
import it.polito.wa2.g07.repositories.UserRepository
import it.polito.wa2.g07.services.UserService
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegisterUserTests {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var activationRepository: ActivationRepository

    @BeforeEach
    fun clearRepositories(){
        userRepository.deleteAll()
        activationRepository.deleteAll()
    }
    @Test
    fun rejectInvalidEmails() {

        //no @
        assertThrows<IllegalArgumentException> {
            userService.registerUser(UserDTO(username = "nick",
                    password = "aA1@aA1@",
                    email = "a.b.c"))
        }
        //no .
        assertThrows<IllegalArgumentException> {
            userService.registerUser(UserDTO(username = "nick",
                    password = "aA1@aA1@",
                    email = "a@c"))
        }

        //wrong sequence of . and @
        assertThrows<IllegalArgumentException> {
            userService.registerUser(UserDTO(username = "nick",
                    password = "aA1@aA1@",
                    email = "a.b@"))
        }
    }

    @Test
    fun rejectInvalidPasswordsTest() {

        //too short
        assertThrows<IllegalArgumentException> {
            userService.registerUser(UserDTO(username = "nick",
                    password = "a",
                    email = "a@b.com"))
        }
        //empty
        assertThrows<IllegalArgumentException> {
            userService.registerUser(UserDTO(
                    username = "nick",
                    password = "",
                    email = "a@b.com"))
        }
        //no uppercase
        assertThrows<IllegalArgumentException> {
            userService.registerUser(UserDTO(
                    username = "nick",
                    password = "aa!!9900",
                    email = "a@b.com"))
        }
        //no lowercase
        assertThrows<IllegalArgumentException> {
            userService.registerUser(UserDTO(username = "nick",
                    password = "AA!!9900",
                    email = "a@b.com"))
        }
        //no digit
        assertThrows<IllegalArgumentException> {
            userService.registerUser(UserDTO(username = "nick",
                    password = "aa!!BBcc",
                    email = "a@b.com"))
        }
        //no non-alphanumeric char
        assertThrows<IllegalArgumentException> {
            userService.registerUser(UserDTO(username = "nick",
                    password = "aa99BBcc",
                    email = "a@b.com"))
        }

        //has whitespaces
        assertThrows<IllegalArgumentException> {
            userService.registerUser(UserDTO(username = "nick",
                    password = "aa&& 99AA",
                    email = "a@b.com"))
        }

    }
    @Test
    fun acceptValidRegisterUserTest() {

        val email = "a@b.com"
        val a = userService.registerUser(UserDTO(username = "user",
                password = "aaBB99!!",
                email = email))

        assertTrue(a.id!=null && a.userEmail == email && a.activationCode!="")
    }

    @Test
    fun rejectDoubleNicknameTest() {
        //TODO: check if validation is necessary
        val username = "nick"
        assertDoesNotThrow { userService.registerUser(UserDTO(
                username=username,
                password =  "aaBB99!!",
                email =  "a@b.com"))
        }

        assertThrows<IllegalArgumentException> {
            userService.registerUser(UserDTO(
                    username = "nick",
                    password = "aaBB99!!",
                    email = "a1@b.com"))
        }
    }

    @Test
    fun rejectDoubleEmailTest() {
        //TODO: check if validation is necessary
        val email = "a1@b.com"
        assertDoesNotThrow { userService.registerUser(UserDTO(
                username="nick",
                password =  "aaBB99!!",
                email =  email))
        }

        assertThrows<IllegalArgumentException> {
            userService.registerUser(UserDTO(
                    username = "nick",
                    password = "aaBB99!!",
                    email = email))
        }
    }
}


