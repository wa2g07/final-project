/*
package it.polito.wa2.g07.statisticsservice.repositories;

import it.polito.wa2.g07.statisticsservice.documents.Transit;
import it.polito.wa2.g07.statisticsservice.dtos.TransitCountDTO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import java.util.Date;

public interface TransitRepositoryJAVA extends ReactiveMongoRepository<Transit, ObjectId> {

    @Aggregation(pipeline = {
            "{'$addFields': {'date': {'$dateToString': {format: '%Y%m%d', date: '$timestamp'}}}}",
            "{'$match': {'date': {'$gte': ?0, '$lte': ?1}}}",
            "{'$project': {'date': 1}}",
            "{'$group': {'_id': '$date', 'count': {'$count': {}}}}"
    })
    Flux<TransitCountDTO> getTransitsCountPerDay(Date from, Date to);

    @Aggregation(pipeline = {
            "{'$addFields': {'date': {'$dateToString': {format: '%Y%m%d', date: '$timestamp'}}}}",
            "{'$match': {'date': ?0}}",
            "{'$addFields': {'hour': {'$hour': '$timestamp'}}}",
            "{'$project': {'hour': 1}}",
            "{'$group': {'_id': '$hour', 'count': {'$count': {}}}}"
    })
    Flux<TransitCountDTO> getTransitsCountPerHour(Date day);

    @Aggregation(pipeline = {
            "{'$match': {'username': ?2}}",
            "{'$addFields': {'date': {'$dateToString': {format: '%Y%m%d', date: '$timestamp'}}}}",
            "{'$match': {'date': {'$gte': ?0, '$lte': ?1}}}",
            "{'$addFields': {'hour': {'$hour': '$timestamp'}}}",
            "{'$project': {'hour': 1}}",
            "{'$group': {'_id': '$hour', 'count': {'$count': {}}}}"
    })
    Flux<TransitCountDTO> getMyTransitsCountPerHour(Date from, Date to, String username);
}



JWT SOLO CUSTOMER
eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJjdXN0b21lciIsImlhdCI6MTY1MTI0MzQ1MiwiZXhwIjoxNjU5OTk5OTk5LCJyb2xlcyI6IkNVU1RPTUVSIn0.7qdzAOQo7NqeG53bd187cXztig3sVafYd_1iXoOKmOFZtjjAmwVIlu0bemtRsuBN

JWT SOLO ADMIN
eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbi1uaWNrIiwiaWF0IjoxNjUxMjQzNDUyLCJleHAiOjE2NTk5OTk5OTksInJvbGVzIjoiQURNSU4ifQ.Y927iZAlQIpADuvCI11gy0mq1z6qq07AdcS5YcmJRKdpuNPkWkuocc_GpyPbBLbt

JWT ENTRAMBI
eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbi1uaWNrIiwiaWF0IjoxNjUxMjQzNDUyLCJleHAiOjE2NTk5OTk5OTksInJvbGVzIjoiQ1VTVE9NRVJ8QURNSU4ifQ.K84pqtp9YuP22PkEUqAk1Fxp8JHSagQWVSEPqka9I6xMwql5OjV-DwiAYpfa6cFP

JWT EMBEDDED SYSTEM:
eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJjdXN0b21lciIsImlhdCI6MTY1MTI0MzQ1MiwiZXhwIjoxNjU5OTk5OTk5LCJyb2xlcyI6IkVNQkVEREVEU1lTVEVNIn0.kNN0WMynsJq9Sal_rBIW5rXCA6r1sxGOVGjc0Q3puphiCHPUh3diEOk0EHpTagfI


db.transits.aggregate([
        {$addFields: {date: {$dateToString: {format: "%Y%m%d", date: "$timestamp"}}}},
        {$match: {date: {$gte: "20220701", $lte: "20220722"}}},
        {$project: {date: 1}},
        {$group: {_id: "$date", count: {$count: {}}}}
])

db.transits.aggregate([
    {$addFields: {date: {$dateToString: {format: "%Y%m%d", date: "$timestamp"}}}},
    {$match: {date: "20220722"}},
    {$addFields: {hour: {$hour: "$timestamp"}}},
    {$project: {hour: 1}},
    {$group: {_id: "$hour", count: {$count: {}}}}
])


db.transits.aggregate([
    {$match: {username: "customer2"}},
    {$addFields: {date: {$dateToString: {format: "%Y%m%d", date: "$timestamp"}}}},
    {$match: {date: {$gte: "20220701", $lte: "20220722"}}},
    {$addFields: {hour: {$hour: "$timestamp"}}},
    {$project: {hour: 1}},
    {$group: {_id: "$hour", count: {$count: {}}}}
])
*/