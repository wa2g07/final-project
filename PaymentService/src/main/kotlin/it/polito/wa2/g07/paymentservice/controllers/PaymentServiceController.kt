package it.polito.wa2.g07.paymentservice.controllers

import it.polito.wa2.g07.paymentservice.dtos.TransactionDTO
import it.polito.wa2.g07.paymentservice.dtos.toDTO
import it.polito.wa2.g07.paymentservice.services.TransactionService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@RestController
class PaymentServiceController(val transactionService: TransactionService) {

    @GetMapping("/admin/transactions", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    suspend fun getAllTransactions(): Flow<TransactionDTO> {
        try {
            return transactionService.getAllTransactions().map { it.toDTO() }.asFlow()
        } catch(e: Exception){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/transactions", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    suspend fun getMyTransactions(@AuthenticationPrincipal principal: Mono<String>): Flow<TransactionDTO> {
        try {
            return transactionService.getTransactionsByOwner(principal).map { it.toDTO() }.asFlow()
        } catch(e: Exception){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }
}