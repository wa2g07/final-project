package it.polito.wa2.g07.statisticsservice.controllers

import it.polito.wa2.g07.statisticsservice.dtos.DoubleCountDTO
import it.polito.wa2.g07.statisticsservice.dtos.LongCountDTO
import it.polito.wa2.g07.statisticsservice.services.StatisticsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.text.SimpleDateFormat

@RestController
class StatisticsController(val statisticsService: StatisticsService) {

    /*
    Returns the number of transit for each day in the given interval of days.
    EXAMPLE REQUEST URL:
    /admin/statistics/transits/perDay?from=20220701&to=20220710
    EXAMPLE FLOW RESPONSE:
    "20220701": 200,
    "20220703": 180
    ...
     */
    @GetMapping(value = ["/admin/statistics/transits/perDay"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getTransitsCountPerDay(@RequestParam("from") from: String, @RequestParam("to") to: String) : Flow<LongCountDTO> {
        return statisticsService.getTransitCountPerDay(SimpleDateFormat("yyyyMMdd").parse(from), SimpleDateFormat("yyyyMMdd").parse(to)).asFlow()
    }

    /*
    Returns the number of transits in each hour of the given day.
    EXAMPLE REQUEST URL:
    /admin/statistics/transits/perHour?date=20220701
    EXAMPLE FLOW RESPONSE:
    "0": 2,
    "1": 3,
    "18": 9
    ...
     */
    @GetMapping(value = ["/admin/statistics/transits/perHour"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getTransitsCountPerHour(@RequestParam("date") date: String): Flow<LongCountDTO> {
        return statisticsService.getTransitCountPerHour(SimpleDateFormat("yyyyMMdd").parse(date)).asFlow()
    }

    /*
    Returns the number of transits in each hour aggregating data for each day in the given time period.
    EXAMPLE REQUEST URL:
    /my/statistics/transits/perDay?from=20220701&to=20220703
    EXAMPLE FLOW RESPONSE:
    "8": 45,
    "12": 32,
    "13": 91
    ...
     */
    @GetMapping(value = ["/my/statistics/transits/perHour"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    suspend fun getMyTransitsCountPerHour(@RequestParam("from") from: String, @RequestParam("to") to: String, @AuthenticationPrincipal principal: Mono<String>): Flow<LongCountDTO> {
        return statisticsService.getMyTransitCountPerHour(SimpleDateFormat("yyyyMMdd").parse(from), SimpleDateFormat("yyyyMMdd").parse(to), principal.awaitSingle()).asFlow()
    }

    /*
    Returns total amount of revenues per each month in the given year
    EXAMPLE REQUEST URL:
    /admin/statistics/revenues/perMonth?year=2022
    EXAMPLE FLOW RESPONSE:
    "1": 120.45,
    "2": 189.09,
    ...
    */
    @GetMapping(value = ["/admin/statistics/revenues/perMonth"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getRevenuesPerMonth(@RequestParam("year") year: String): Flow<DoubleCountDTO>{
        return statisticsService.getRevenuesPerMonth(year).asFlow()
    }
    /*
    Returns total amount of expenses per each month in the given year for the logged in user
    EXAMPLE REQUEST URL:
    /my/statistics/expenses/perMonth?year=2022
    EXAMPLE FLOW RESPONSE:
    "1": 10.80,
    "2": 12.09,
    ...
*/
    @GetMapping(value = ["/my/statistics/expenses/perMonth"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    suspend fun getMyExpensesPerMonth(@RequestParam("year") year: String, @AuthenticationPrincipal principal: Mono<String>): Flow<DoubleCountDTO>{
        return statisticsService.getMyExpensesPerMonth(year, username = principal.awaitSingle()).asFlow()
    }

    /*
    Returns the top <limit> users in terms of number of tickets bought in the given year
    EXAMPLE REQUEST URL:
    /admin/statistics/topBuyers?limit=2&year=2022
    EXAMPLE FLOW RESPONSE:
    "customer1": 102,
    "customer2": 88,
    */
    @GetMapping(value = ["/admin/statistics/topBuyers"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getTopBuyers(@RequestParam("limit") limit: Int, @RequestParam("year") year: String): Flow<LongCountDTO>{
        return statisticsService.getTopBuyers(limit, year).asFlow()
    }
}

