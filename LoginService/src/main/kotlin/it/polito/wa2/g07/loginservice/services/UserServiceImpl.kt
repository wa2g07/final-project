package it.polito.wa2.g07.loginservice.services

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import it.polito.wa2.g07.loginservice.dtos.ActivationDTO
import it.polito.wa2.g07.loginservice.dtos.UserDTO
import it.polito.wa2.g07.loginservice.dtos.toDTO
import it.polito.wa2.g07.loginservice.entities.Activation
import it.polito.wa2.g07.loginservice.entities.User
import it.polito.wa2.g07.loginservice.repositories.ActivationRepository
import it.polito.wa2.g07.loginservice.repositories.UserRepository
import it.polito.wa2.g07.loginservice.utils.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.annotation.PostConstruct
import javax.crypto.SecretKey

@Service
@Transactional
class UserServiceImpl(
  val emailService: EmailService,
  val userRepository: UserRepository,
  val activationRepository: ActivationRepository
) : UserService {

  @Value("\${secrets.jwtkey}")
  lateinit var b64Key: String

  //val b64Key = "TWMjLVp5MmQkRnhzRk5oOEdiNTVTczJQQGRrOUVHI3lUcjU/VVdDKw=="

  lateinit var key: SecretKey

  @PostConstruct
  fun initKey() {
    key =
      Keys.hmacShaKeyFor(Decoders.BASE64.decode(b64Key))
  }

  override fun registerUser(userDTO: UserDTO): ActivationDTO {
    if (!checkEmail(userDTO.email) || !checkPassword(userDTO.password)) {
      throw IllegalArgumentException("wrong DTO")
    }

    if (userRepository.existsUserByEmail(userDTO.email)) {
      throw IllegalArgumentException("user with this email already exists")
    }

    if (userRepository.existsUserByUsername(userDTO.username)) {
      throw IllegalArgumentException("user with this nickname already exists")
    }

    val user = User(
      username = userDTO.username,
      password = userDTO.password,
      email = userDTO.email
    )
    val u = userRepository.save(
      user
    )
    val a = activationRepository.save(Activation(user = u))
    try {
      emailService.sendConfirmationEmail(
        email = a.user!!.email,
        activationCode = a.activationCode,
        activationID = a.id.toString()
      )
    } catch (e: Exception) {
      return a.toDTO()
    }
    return a.toDTO()

  }

  override fun registerAdmin(userDTO: UserDTO, superadminName: String): UserDTO {
    if (!checkEmail(userDTO.email) || !checkPassword(userDTO.password)) {
      throw IllegalArgumentException("wrong DTO")
    }

    if (userRepository.existsUserByEmail(userDTO.email)) {
      throw IllegalArgumentException("user with this email already exists")
    }

    if (userRepository.existsUserByUsername(userDTO.username)) {
      throw IllegalArgumentException("user with this nickname already exists")
    }

    val superadmin = userRepository.getUserByUsername(superadminName)
    if (!BCrypt.checkpw(
        "admin",
        superadmin.password
      )
    ) throw IllegalArgumentException("superadmin must change her password")

    val user = User(
      username = userDTO.username,
      password = userDTO.password,
      email = userDTO.email,
      roles = Role.SUPERADMIN.printableName
    )
    val u = userRepository.save(
      user
    )

    return u.toDTO()
  }

  override fun validateActivationRequest(activationDTO: ActivationDTO): UserDTO? {

    val a: Activation

    if (!activationRepository.existsById(UUID.fromString(activationDTO.id))) {
      throw IllegalArgumentException("activation not found")
    } else
      a = activationRepository.findById(UUID.fromString(activationDTO.id)).get()

    if (a.user!!.activated) {
      a.detachUser()
      activationRepository.delete(a)
      return null
    }
    if (currentTime() > a.activationDeadline) {
      userRepository.delete(a.user!!)
      return null
    } else if (activationDTO.activationCode != a.activationCode) {
      a.attemptsCounter -= 1
      return if (a.attemptsCounter == 0) {
        userRepository.delete(a.user!!)
        null
      } else {
        null
      }
    }
    a.user!!.activated = true
    val uDTO = UserDTO(
      id = a.user!!.id,
      username = a.user!!.username,
      email = a.user!!.email,
    )
    a.detachUser()
    activationRepository.delete(a)
    return uDTO

  }

  override fun loginUser(userDTO: UserDTO): String {
    val u = userRepository.getUserByUsername(userDTO.username)
    if (BCrypt.checkpw(userDTO.password, u.password) && u.activated) {
      return makeJWT(
        sub = u.username,
        iat = currentTime(),
        exp = makeDate(60),
        roles = u.roles,
        key = key
      )
    }
    throw IllegalArgumentException()
  }
}