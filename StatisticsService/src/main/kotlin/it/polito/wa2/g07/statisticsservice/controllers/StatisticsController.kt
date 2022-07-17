package it.polito.wa2.g07.statisticsservice.controllers

import it.polito.wa2.g07.statisticsservice.dtos.TransitCountDTO
import it.polito.wa2.g07.statisticsservice.services.StatisticsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class StatisticsController(val statisticsService: StatisticsService) {

    /*
    Returns the number of transit for each day in the given interval of days.
    EXAMPLE REQUEST URL:
    /admin/statistics/transits/perDay?from=20220701&to=20220703
    EXAMPLE FLOW RESPONSE:
    "20220701": 200,
    "20220702": 30,
    "20220703": 180
     */
    @GetMapping(value = ["/admin/statistics/transits/perDay"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getTransitsCountPerDay(@RequestParam("from") from: String, @RequestParam("to") to: String) : Flow<TransitCountDTO> {

        return emptyFlow()
    }

    /*
    Returns the number of transits in each hour of the given day.
    EXAMPLE REQUEST URL:
    /admin/statistics/transits/perHour?date=20220701
    EXAMPLE FLOW RESPONSE:
    "00:": 12,
    "01:": 3,
    "02:": 0
    ...
     */
    @GetMapping(value = ["/admin/statistics/transits/perHour"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getTransitsCountPerHour(@RequestParam("date") date: String): Flow<TransitCountDTO> {
        return emptyFlow()
    }
}