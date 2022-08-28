package it.polito.wa2.g07.loginservice.utils

data class ActivationUser (val activation_code: String, val provisional_id: String)
data class RegistrationUser(val nickname: String, val email: String, val password: String)
data class RegistrationAdmin(val nickname: String, val email: String, val password: String, val role: Role)
data class LoginUser(val username: String, val password: String)
data class UpdatePassword(val oldpassword: String, val newpassword: String)

data class RegistrationResponse(val provisional_id: String, val email: String)
data class AdminRegistrationResponse(val provisional_id: Long, val email: String)
data class ValidationResponse(val userId: Long, val nickname: String, val email: String)