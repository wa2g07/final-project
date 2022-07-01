package it.polito.wa2.g07.controllersTests

import it.polito.wa2.g07.entities.Activation
import it.polito.wa2.g07.entities.User
import it.polito.wa2.g07.repositories.ActivationRepository
import it.polito.wa2.g07.repositories.UserRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.json.BasicJsonParser
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.lang.Thread.sleep
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


@Testcontainers
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class RegistrationControllerTests {

    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") {"create-drop"}
        }
    }

    @LocalServerPort
    protected var port: Int = 0
    @Autowired
    lateinit var restTemplate: TestRestTemplate
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var activationRepository : ActivationRepository

    @BeforeEach
    fun initRepositories(){
        userRepository.deleteAll()
        activationRepository.deleteAll()
    }

    @Test
    fun rejectDoubleEmail() {
        userRepository.save(User(username ="nick", email = "a@b.c", activated = true, password = "AAbb99!!"))
        val baseUrl = "http://localhost:$port"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = "{ \"nickname\": \"nick2\", \"password\": \"AAbb99!!\", \"email\": \"a@b.c\"}"
        val entity : HttpEntity<String> = HttpEntity<String>(request, headers)
        val response = restTemplate.postForEntity<String>("$baseUrl/user/register", entity )
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
    }

    @Test
    fun rejectDoubleNickname() {
        userRepository.save(User(username ="nick", email = "a@b.c", activated = true, password = "AAbb99!!"))
        val baseUrl = "http://localhost:$port"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = "{ \"nickname\": \"nick\", \"password\": \"AAbb99!!\", \"email\": \"a1@b.c\"}"
        val entity : HttpEntity<String> = HttpEntity<String>(request, headers)
        val response = restTemplate.postForEntity<String>("$baseUrl/user/register", entity )
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
    }

    @Test
    fun rejectMissingField() {
        val baseUrl = "http://localhost:$port"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = "{ \"nickname\": \"nick\", \"email\": \"a1@b.c\"}"
        val entity : HttpEntity<String> = HttpEntity<String>(request, headers)
        val response = restTemplate.postForEntity<String>("$baseUrl/user/register", entity )
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
    }

    @Test
    fun rejectInvalidEmail() {
        val baseUrl = "http://localhost:$port"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = "{ \"nickname\": \"nick\", \"password\": \"AAbb99!!\", \"email\": \"ab.c\"}"
        val entity : HttpEntity<String> = HttpEntity<String>(request, headers)
        val response = restTemplate.postForEntity<String>("$baseUrl/user/register", entity )
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
    }

    @Test
    fun rejectWeakPassword() {
        val baseUrl = "http://localhost:$port"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = "{ \"nickname\": \"nick\", \"password\": \"AAbB9\", \"email\": \"a@b.c\"}"
        val entity : HttpEntity<String> = HttpEntity<String>(request, headers)
        val response = restTemplate.postForEntity<String>("$baseUrl/user/register", entity )
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
    }

    @Test
    fun rejectOutOfTimeValidation(){
        val user = userRepository.save(User(username ="nick", email = "a@b.c", /*activated = true,*/ password = "AAbb99!!")) //if it is activated then userRepository.count() == 1 should be correct
        val activation = activationRepository.save(Activation(user = user, activationDeadline = Date(LocalDateTime.now().plusSeconds(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())))
        sleep(5000) //it should not be necessary, but just to be sure wait 5 seconds
        val baseUrl = "http://localhost:$port"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = "{ \"provisional_id\": \"${activation.id}\", \"activation_code\": \"${activation.activationCode}\"}"
        val entity : HttpEntity<String> = HttpEntity<String>(request, headers)
        val response = restTemplate.postForEntity<String>("$baseUrl/user/validate", entity )
        assert(response.statusCode == HttpStatus.NOT_FOUND)
        assert(userRepository.count() == 0L)
        assert(activationRepository.count() == 0L)
    }

    @Test
    fun rejectNonExistingProvisionalId(){
        val user = userRepository.save(User(username ="nick", email = "a@b.c", activated = true, password = "AAbb99!!"))
        val activation = activationRepository.save(Activation(user = user))
        val baseUrl = "http://localhost:$port"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        //prepend the X letter to the provisional_id just to be sure that it is different from the one saved
        val request = "{ \"provisional_id\": \"X${activation.id}\", \"activation_code\": \"${activation.activationCode}\"}"
        val entity : HttpEntity<String> = HttpEntity<String>(request, headers)
        val response = restTemplate.postForEntity<String>("$baseUrl/user/validate", entity )
        assert(response.statusCode == HttpStatus.NOT_FOUND)
    }

    @Test
    fun rejectWrongActivationCodeAndMoreThanAllowedTries(){
        val user = userRepository.save(User(username ="nick", email = "a@b.c", /*activated = true,*/ password = "AAbb99!!"))
        val activation = activationRepository.save(Activation(user = user))
        val baseUrl = "http://localhost:$port"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        //prepend the X letter to the activation_code just to be sure that it is different from the one saved
        val request = "{ \"provisional_id\": \"${activation.id}\", \"activation_code\": \"X${activation.activationCode}\"}"
        val entity : HttpEntity<String> = HttpEntity<String>(request, headers)
        val response = restTemplate.postForEntity<String>("$baseUrl/user/validate", entity )
        assert(response.statusCode == HttpStatus.NOT_FOUND)
        restTemplate.postForEntity<String>("$baseUrl/user/validate", entity )
        restTemplate.postForEntity<String>("$baseUrl/user/validate", entity )
        restTemplate.postForEntity<String>("$baseUrl/user/validate", entity )
        restTemplate.postForEntity<String>("$baseUrl/user/validate", entity )
        assert(userRepository.count() == 0L)
        assert(activationRepository.count() == 0L)
    }

    @Test
    fun rejectTooManyRequests(){
        val baseUrl = "http://localhost:$port"
        var response : ResponseEntity<String>?
        var count = 0
        for(i in 1..50) {
            response = restTemplate.postForEntity("$baseUrl/user/register", "request_body")
            if(response.statusCode == HttpStatus.TOO_MANY_REQUESTS)
                count++
        }
        assert(count >= 1)
        count = 0
        for(i in 1..50) {
            response = restTemplate.postForEntity("$baseUrl/user/validate", "request_body")
            if(response.statusCode == HttpStatus.TOO_MANY_REQUESTS)
                count++
        }
        assert(count >= 1)
    }

    @Test
    fun testPeriodicCleanUp(){
        val user = userRepository.save(User(username ="nick", email = "a@b.c", password = "AAbb99!!"))
        activationRepository.save(Activation(user = user, activationDeadline = Date(LocalDateTime.now().plusSeconds(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())))
        sleep(35000) //wait 35 sec, since the time periodic cleaning is activated every 30 sec
        assert(userRepository.count() == 0L)
        assert(activationRepository.count() == 0L)
    }

    @Test
    fun acceptCorrectRegistration() {
        val baseUrl = "http://localhost:$port"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = "{ \"nickname\": \"nick\", \"password\": \"AAbb99!!\", \"email\": \"a@b.c\"}"
        val entity : HttpEntity<String> = HttpEntity<String>(request, headers)
        val response = restTemplate.postForEntity<String>("$baseUrl/user/register", entity )
        assert(response.statusCode == HttpStatus.ACCEPTED)

        var map = BasicJsonParser().parseMap(response.body)
        assert(map["provisional_id"] != null && map["email"] == "a@b.c")
        val activation = activationRepository.findById(UUID.fromString(map["provisional_id"] as String))
        assert(activation.get().attemptsCounter == 5)
        //test that at least 5 minutes are available to complete the registration, check timings in readme.md
        assert(activation.get().activationDeadline >= Date(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
        assert(!activation.get().user!!.activated)
        assert(activation.get().user!!.email == "a@b.c")
        assert(activation.get().user!!.username == "nick")

        val activationCode = activation.get().activationCode
        val provisionalId = activation.get().id

        val request2 = "{ \"provisional_id\": \"$provisionalId\", \"activation_code\": \"$activationCode\"}"
        val headers2 = HttpHeaders()
        headers2.contentType = MediaType.APPLICATION_JSON
        val entity2 : HttpEntity<String> = HttpEntity<String>(request2, headers2)
        val response2 = restTemplate.postForEntity<String>("$baseUrl/user/validate", entity2 )
        assert(response2.statusCode == HttpStatus.CREATED)

        map = BasicJsonParser().parseMap(response2.body)
        assert(map["userId"] != null && map["nickname"] == "nick" && map["email"] == "a@b.c")
        assert(activationRepository.count() == 0L)
        val user = userRepository.findById(map["userId"] as Long)
        assert(user.get().activated)
        assert(user.get().email == "a@b.c")
        assert(user.get().username == "nick")
    }

    @Test
    fun loginSuccessful(){
        acceptCorrectRegistration()
        val baseUrl = "http://localhost:$port"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = "{ \"username\": \"nick\", \"password\": \"AAbb99!!\"}"
        val entity : HttpEntity<String> = HttpEntity<String>(request, headers)
        val response = restTemplate.postForEntity<String>("$baseUrl/user/login", entity )

        assertTrue(response.hasBody())
        assertTrue(response.statusCode ==HttpStatus.OK)
    }

    @Test
    fun loginFailure(){
        acceptCorrectRegistration()
        val baseUrl = "http://localhost:$port"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = "{ \"username\": \"nick\", \"password\": \"wrongPassword\"}"
        val entity : HttpEntity<String> = HttpEntity<String>(request, headers)
        val response = restTemplate.postForEntity<String>("$baseUrl/user/login", entity )
        assertTrue(response.statusCode ==HttpStatus.FORBIDDEN)
    }
}
