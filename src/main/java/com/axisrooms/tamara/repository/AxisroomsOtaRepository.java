package com.axisrooms.tamara.repository;

import com.axisrooms.tamara.model.TransactionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * To interact with transaction logs model (mongo db)
 */
@Repository
public class AxisroomsOtaRepository {

    @Autowired
    private MongoTemplate mongoTemplate;


    public List<TransactionLog> findByHotelId(String hotelId, LocalDate requestDate) {
        Criteria criteria = new Criteria().andOperator(getHotelIdCriteria(hotelId),
                getRequestDateCriteria(requestDate));
        return mongoTemplate.find(Query.query(criteria).limit(1000), TransactionLog.class);
    }

    public List<TransactionLog> findByRoomId(String roomId, LocalDate requestDate) {
        Criteria criteria = new Criteria().andOperator(
                getRoomIdCriteria(roomId),
                getRequestDateCriteria(requestDate));
        return mongoTemplate.find(Query.query(criteria), TransactionLog.class);
    }

    public List<TransactionLog> findByRatePlanId(String ratePlanId, LocalDate requestDate) {
        Criteria criteria = new Criteria().andOperator(
                getRatePlanCriteria(ratePlanId),
                getRequestDateCriteria(requestDate));
        return mongoTemplate.find(Query.query(criteria), TransactionLog.class);
    }

    public TransactionLog findByArcRequestId(String arcRequestId) {
        Criteria criteria = Criteria.where("id").is(arcRequestId);
        return mongoTemplate.findOne(Query.query(criteria), TransactionLog.class);
    }

    public <T> void save(T t) {
        mongoTemplate.save(t);
    }

    private Criteria getRatePlanCriteria(String ratePlanId) {
        return Criteria.where("ratePlanId").is(ratePlanId);
    }

    private Criteria getRoomIdCriteria(String roomId) {
        return Criteria.where("roomId").is(roomId);
    }

    private Criteria getHotelIdCriteria(String hotelId) {
        return Criteria.where("hotelId").is(hotelId);
    }

    private Criteria getRequestDateCriteria(LocalDate requestDate) {
        return Criteria.where("requestDate").is(requestDate);
    }
}
