package it.polito.wa2.g07.repositoriesTests

import it.polito.wa2.g07.entities.Activation
import it.polito.wa2.g07.entities.User
import it.polito.wa2.g07.repositories.ActivationRepository
import it.polito.wa2.g07.repositories.UserRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*


@SpringBootTest
@Transactional
class RepositoryTests {

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var activationRepository: ActivationRepository

    @Test
    fun createUserTest() {
        val u = userRepository.save(User(username="user-test",password="pass-test"))
        val  u2 = userRepository.findById(u.id as Long)

        assertTrue(u2.get().id == u.id)
    }

    @Test
    fun attachActivationToUserTest(){
        val u = userRepository.save(User(username="user-test",password="pass-test"))
        val a = activationRepository.save(Activation(user=u))

        activationRepository.save(Activation())

        u.activation = a

        assertTrue {
            val a2 = activationRepository.findById(a.id as UUID)
            a2.get().user!!.id == u.id
        }
    }

    @Test
    fun checkUserExistByEmailTest(){
        userRepository.save(User(username="user-test",password="pass-test",email="user1@test.com"))

        assertTrue(userRepository.existsUserByEmail("user1@test.com"))
        assertTrue(!userRepository.existsUserByEmail("notexistemail@test.com"))
    }

    @Test
    fun deleteActivationTest(){
        val u = userRepository.save(User(username="user-test3",password="pass-test3"))
        val a = activationRepository.save(Activation(user = u))
        val u2 = userRepository.save(User(username="user2-test3",password="pass2-test3"))
        val a2 = activationRepository.save(Activation(user = u2))
        val aUUIDString = a.id.toString()
        val a2UUIDString = a2.id.toString()

        //a.detachUser()
        activationRepository.delete(a)
        activationRepository.deleteById(UUID.fromString(a2UUIDString))

        assertTrue { !activationRepository.existsById(UUID.fromString(aUUIDString)) }
        assertTrue { userRepository.findById(u.id!!).isPresent }
        assertTrue { !activationRepository.existsById(UUID.fromString(a2UUIDString)) }
        assertTrue { userRepository.findById(u2.id!!).isPresent }

        userRepository.delete(a2.user!!)
        assertTrue { !userRepository.findById(u2.id!!).isPresent }
    }

    @Test
    fun modifyAttemptCounterTest(){
        val u = userRepository.save(User(username="user-test3",password="pass-test3"))
        val a = activationRepository.save(Activation(user = u))

        assertTrue { a.attemptsCounter == 5 }

        a.attemptsCounter -= 1

        val a2 = activationRepository.findById(a.id!!).get()

        assertTrue { a2.attemptsCounter == 4 }
    }

    @Test
    fun multipleUsersSamePasswordTest(){
        val u1 = userRepository.save(User(username="user-test4",password="aRandomPassword123!"))
        val u2 = userRepository.save(User(username="user-test5",password="aRandomPassword123!"))
        val u3 = userRepository.save(User(username="user-test6",password="aRandomPassword123!"))

        assertTrue { u1.password!= u2.password &&
                u2.password != u3.password &&
                u3.password != u1.password
        }
    }
}