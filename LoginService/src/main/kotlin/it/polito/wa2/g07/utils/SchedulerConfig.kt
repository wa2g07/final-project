package it.polito.wa2.g07.utils

import it.polito.wa2.g07.repositories.ActivationRepository
import it.polito.wa2.g07.repositories.UserRepository
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

  //a cleaning process occurs each 30 second, just for test purposes, in order to avoid tests to sleep for too long
  //a reasonable value in production would be every 24 hours
  @Scheduled(fixedDelay = 30000) // 30 sec
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