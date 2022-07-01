package it.polito.wa2.g07.servicesTests

import it.polito.wa2.g07.services.EmailService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class EmailServiceTests {
    @Autowired
    lateinit var emailService: EmailService

    @Test
    fun sendEmailTest(){
        assertDoesNotThrow { emailService.sendConfirmationEmail(
                email="test@gmail.com", //put here a mail you are in control of to test proper sending of the email
                activationCode = "<here is the code>",
                activationID = "<here is the ID>") }
    }
}