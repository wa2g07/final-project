package it.polito.wa2.g07.loginservice.utils

import it.polito.wa2.g07.loginservice.repositories.ActivationRepository
import it.polito.wa2.g07.loginservice.repositories.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = ["scheduler.enabled"], matchIfMissing = true)
class SchedulerConfig

@Service
@Transactional
class PruneDatabase(
    val userRepository: UserRepository,
    val activationRepository: ActivationRepository
) {
  val logger: Logger =
    LoggerFactory.getLogger("PruneDatabase")

  @Scheduled(fixedDelay = 3600000) // 1 hour
  fun pruneExpiredRegistration() {
    logger.info(
      "[i] Computing pruning at " +
          LocalDateTime.now()
    )

    val a = activationRepository.getActivationByActivationDeadlineBeforeDate(currentTime())
    for (e in a) {
      logger.info(
        "[i] Deleting activation code ${e.activationCode} for user ${e.user}"
      )
      userRepository.delete(e.user!!)
      activationRepository.delete(e)
    }
  }
}
