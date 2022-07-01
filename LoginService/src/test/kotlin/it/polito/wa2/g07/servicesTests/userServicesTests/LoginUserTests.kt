package it.polito.wa2.g07.servicesTests.userServicesTests

import it.polito.wa2.g07.dtos.UserDTO
import it.polito.wa2.g07.entities.User
import it.polito.wa2.g07.repositories.UserRepository
import it.polito.wa2.g07.services.UserService
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginUserTests {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userRepository: UserRepository


    @BeforeEach
    fun clearRepositoriesAndInitialize(){
        userRepository.deleteAll()
    }

     @Test
     fun loginSuccessfulTest(){
        val username = "user"
        val password = "Random123Password!"
        userRepository.save(User(username=username,password=password, activated = true))
        assertDoesNotThrow { userService.loginUser(UserDTO(username=username, password = password)) }
        val jwt = userService.loginUser(UserDTO(username=username, password = password))
     }

    @Test
    fun loginFailureTest(){
        val username = "user"
        val username2 = "user2"
        val password = "Random123Password!"
        userRepository.save(User(username=username,password="Random123Password!", activated = false))
        userRepository.save(User(username=username2,password="Random123Password!", activated = true))
        assertThrows<IllegalArgumentException> {
            userService.loginUser(UserDTO(username=username, password = password))
        }
        assertThrows<IllegalArgumentException> {
            userService.loginUser(UserDTO(username=username2, password = "${password}a"))
        }
    }
}
