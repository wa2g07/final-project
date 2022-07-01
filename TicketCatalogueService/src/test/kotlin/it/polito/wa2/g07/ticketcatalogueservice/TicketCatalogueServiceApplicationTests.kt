package it.polito.wa2.g07.ticketcatalogueservice
/*
import it.polito.wa2.g07.ticketcatalogueservice.documents.Order
import it.polito.wa2.g07.ticketcatalogueservice.dtos.TicketDTO
import it.polito.wa2.g07.ticketcatalogueservice.utils.ProfileResponse
import it.polito.wa2.g07.ticketcatalogueservice.utils.TicketType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.reactive.function.client.bodyToFlow


@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicketCatalogueServiceApplicationTests {

    lateinit var webClient : WebClient

    @LocalServerPort
    val port: Int = 0

    val adminHeader = "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbi1uaWNrIiwiaWF0IjoxNjUxMjQzNDUyLCJleHAiOjE2NTk5OTk5OTksInJvbGVzIjoiQURNSU4ifQ.Y927iZAlQIpADuvCI11gy0mq1z6qq07AdcS5YcmJRKdpuNPkWkuocc_GpyPbBLbt"
    val customerHeader = "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJjdXN0b21lciIsImlhdCI6MTY1MTI0MzQ1MiwiZXhwIjoxNjU5OTk5OTk5LCJyb2xlcyI6IkNVU1RPTUVSIn0.7qdzAOQo7NqeG53bd187cXztig3sVafYd_1iXoOKmOFZtjjAmwVIlu0bemtRsuBN"
    @Test
    fun postAdminTicketsTest() {
        var endpoint = "/admin/tickets"
        var uri = "http://127.0.0.1:${port}${endpoint}"
        var res = webClient.post().uri(uri).
        accept(MediaType.APPLICATION_NDJSON).
        header(HttpHeaders.AUTHORIZATION, adminHeader).
        bodyValue(TicketType(3.0.toFloat(),"type1",null)).
        retrieve().bodyToMono(TicketDTO::class.java).block()

        val tId = res!!.id
        endpoint = "/tickets"
        uri = "http://127.0.0.1:${port}${endpoint}"
        var res2 = webClient.get().uri(uri).
        accept(MediaType.APPLICATION_NDJSON).
        header(HttpHeaders.AUTHORIZATION, adminHeader).
                retrieve().bodyToFlux(TicketDTO::class.java).filter { it.id == tId }.blockFirst()

        assertTrue(res2 != null)
    }

    @Test
    fun postShopTicketTest(){

    }


}
*/