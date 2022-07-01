package it.polito.wa2.g07.services

interface EmailService {
    fun sendConfirmationEmail(email:String,activationCode:String,activationID:String) : Boolean
}