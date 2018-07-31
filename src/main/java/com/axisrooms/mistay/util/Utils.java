package com.axisrooms.mistay.util;

import com.axisrooms.mistay.bean.RequestResponseData;
import com.axisrooms.mistay.enums.OTA;
import com.axisrooms.mistay.enums.Operation;
import com.axisrooms.mistay.enums.ServiceName;
import com.axisrooms.mistay.model.TransactionLog;
import com.axisrooms.mistay.request.InventoryRequest;
import com.axisrooms.mistay.request.PriceRequest;
import com.axisrooms.mistay.request.RestrictionRequest;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class Utils {
    public static void isValid(String token, String hotelId, String acceptedToken) {
        Preconditions.checkArgument(!StringUtils.isEmpty(token), "Token cannot be null or empty");
        Preconditions.checkArgument(token.equals(acceptedToken), "Token is incorrect");
        Preconditions.checkArgument(!StringUtils.isEmpty(hotelId), "HotelId cannot be null or empty");
    }

    public static void addOTAResponse(String response, TransactionLog transactionLog) throws Exception {
        addOTAResponse(transactionLog, response);
    }

    public static void addOTAResponse(Throwable throwable, TransactionLog transactionLog) throws Exception {
        addOTAResponse(transactionLog, Throwables.getStackTraceAsString(throwable));
    }

    private static void addOTAResponse(TransactionLog transactionLog, String string) {
        RequestResponseData otaData = transactionLog.getOtaData();
        otaData.setResponseTime(Instant.now());
        otaData.setResponse(string);
    }

    public static void addCommonData(InventoryRequest inventoryRequest, TransactionLog transactionLog) {
        addCommonData(transactionLog, inventoryRequest.getArcRequestId(), inventoryRequest.getHotelId(), Operation.INVENTORY_UPDATE,
                inventoryRequest.getData().get(0).getRoomId());
    }

    public static void addCommonData(PriceRequest priceRequest, TransactionLog transactionLog) {
        addCommonData(transactionLog, priceRequest.getArcRequestId(), priceRequest.getHotelId(), Operation.PRICE_UPDATE,
                priceRequest.getData().get(0).getRoomDetails().get(0).getRoomId());
        transactionLog.setRatePlanId(priceRequest.getData().get(0).getRoomDetails().get(0).getRatePlanDetails().get(0).getRatePlanId());
    }

    public static void addCommonData(RestrictionRequest restrictionRequest, TransactionLog transactionLog) {
        addCommonData(transactionLog, restrictionRequest.getArcRequestId(), restrictionRequest.getHotelId(), Operation.RESTRICTION_UPDATE,
                restrictionRequest.getData().get(0).getRoomDetails().get(0).getRoomId());
        transactionLog.setRatePlanId(restrictionRequest.getData().get(0).getRoomDetails().get(0).getRatePlanDetails().get(0).getRatePlanId());
    }

    private static void addCommonData(TransactionLog transactionLog, String id, String hotelId, Operation operation, String roomId) {
        transactionLog.setId(id);
        transactionLog.setHotelId(hotelId);
        transactionLog.setOperation(operation);
        transactionLog.setOta(OTA.WANDERTRAILS);
        transactionLog.setDate(LocalDate.now());
        transactionLog.setService(ServiceName.WANDERTRAILS_SERVICE);
        transactionLog.setRoomId(roomId);
    }

    public static void addOTARequest(String otaRequest, TransactionLog transactionLog) throws Exception {
        RequestResponseData detail = new RequestResponseData();
        detail.setRequest(otaRequest);
        detail.setRequestTime(Instant.now());
        transactionLog.setOtaData(detail);
    }

    public static <T> void addCMResponse(T t, TransactionLog transactionLog) {
        RequestResponseData cmData = transactionLog.getCmData();
        cmData.setResponseTime(Instant.now());
        cmData.setResponse(t);
    }

    public static <T> void addCMRequest(T t, TransactionLog transactionLog) {
        RequestResponseData detail = new RequestResponseData();
        detail.setRequest(t);
        detail.setRequestTime(Instant.now());
        transactionLog.setCmData(detail);
    }

    public static void validateDates(LocalDate startDate, LocalDate endDate) {
        Preconditions.checkArgument(startDate != null, "StartDate cannot be null");
        Preconditions.checkArgument(endDate != null, "EndDate cannot be null");
        Preconditions.checkArgument(validateStartAndEndDates(startDate, endDate), "StartDate or EndDate is in the past.");
    }

    public static boolean validateStartAndEndDates(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(Constants.DATE_PATTERN);
        LocalDate todaysDate = LocalDate.parse(LocalDate.now().toString(), df);
        return (startDate.isEqual(todaysDate) || startDate.isAfter(todaysDate))
                && (endDate.isEqual(todaysDate) || endDate.isAfter(todaysDate))
                && (endDate.isEqual(startDate) || endDate.isAfter(startDate));
    }

    public static String getBasicAuthentication(String userName, String password){
        String credentails = userName + ":" + password;
        String basicAuthString = new String(Base64.encodeBase64(credentails.getBytes()));
        return basicAuthString;
    }
}
