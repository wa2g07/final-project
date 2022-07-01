package it.polito.wa2.g07.loginservice.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl : EmailService {
    @Autowired
    lateinit var mailSender: JavaMailSender

    override fun sendConfirmationEmail(email: String, activationCode: String, activationID: String): Boolean {
        val message = SimpleMailMessage()
        message.setTo(email)
        message.setSubject("Confirm your registration request")
        message.setText("Your activation code is: $activationCode\nYour activation id is: $activationID")
        mailSender.send(message)
        return true
    }
}