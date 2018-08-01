package com.axisrooms.mistay.manager;

import com.axisrooms.mistay.bean.*;
import com.axisrooms.mistay.enums.Occupancy;
import com.axisrooms.mistay.generated.RatePlanInfo.RatePlanInfoRequest;
import com.axisrooms.mistay.generated.RatePlanInfo.RatePlanInfoResponse;
import com.axisrooms.mistay.generated.RatePlanInfo.Validity;
import com.axisrooms.mistay.generated.productInfo.Datum;
import com.axisrooms.mistay.generated.productInfo.ProductInfoRequest;
import com.axisrooms.mistay.generated.productInfo.ProductInfoResponse;
import com.axisrooms.mistay.generated.updateInventory.Auth;
import com.axisrooms.mistay.generated.updateInventory.Data;
import com.axisrooms.mistay.generated.updateInventory.InventoryUpdate;
import com.axisrooms.mistay.generated.updateInventory.InventoryUpdateResponse;
import com.axisrooms.mistay.generated.updatePrice.PriceUpdateResponse;
import com.axisrooms.mistay.generated.updatePrice.Rate;
import com.axisrooms.mistay.generated.updatePrice.RateUpdateResponse;
import com.axisrooms.mistay.generated.updatePrice.UpdatePriceOTA;
import com.axisrooms.mistay.model.TransactionLog;
import com.axisrooms.mistay.repository.AxisroomsOtaRepository;
import com.axisrooms.mistay.request.InventoryRequest;
import com.axisrooms.mistay.request.PriceRequest;
import com.axisrooms.mistay.request.RestrictionRequest;
import com.axisrooms.mistay.response.*;
import com.axisrooms.mistay.util.MarshalUnmarshalUtils;
import com.axisrooms.mistay.util.OccupancyNotSupportedException;
import com.axisrooms.mistay.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.axisrooms.mistay.util.Constants.SUCCESS;

/**
 * actual call to the external api in ota side will happen from here
 */
@Service
@Slf4j
public class MiStayOtaManager implements OTAManager {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${getRoomsUrl}")
    private String getRoomsUrl;

    @Value("${getUpdateInvUrl}")
    private String getUpdateInvUrl;

    @Value("${getUpdatePriceUrl}")
    private String getUpdatePriceUrl;

    @Value("${getProductInfoUrl}")
    private String getProductInfoUrl;

    @Value("${mistay-ota.communication.userName}")
    private String username;

    @Value("${mistay-ota.communication.password}")
    private String password;

    @Value("${mistay-ota.communication.apitoken}")
    private String apiKey;

    @Autowired
    private AxisroomsOtaRepository repository;

    @Override
    public RoomResponse getRoomList(String hotelId) throws Exception {
        ProductInfoRequest productInfoRequest = buildProductInfoRequest(hotelId);
        ProductInfoResponse productInfoResponse = getProductInfo(productInfoRequest);
        RoomResponse roomResponse = buildRoomResponse(productInfoResponse);
        return roomResponse;
    }

    @Override
    public RatePlanResponse getRatePlans(String hotelId, String roomId) throws Exception {
//        RatePlanInfoRequest ratePlanInfoRequest = buildRatePlanInfoRequest(hotelId, roomId);
        ProductInfoRequest productInfoRequest = buildProductInfoRequest(hotelId);
//        RatePlanInfoResponse ratePlanInfoResponse = getRatePlanInfo(ratePlanInfoRequest);
        ProductInfoResponse productInfoResponse = getProductInfo(productInfoRequest);
        RatePlanResponse ratePlanResponse = buildRatePlansResponse(productInfoResponse, roomId);
        return ratePlanResponse;
    }

    @Override
    public InventoryResponse updateInventory(InventoryRequest inventoryRequest) throws Exception {
        TransactionLog transactionLog = new TransactionLog();
        Utils.addCommonData(inventoryRequest, transactionLog);
        InventoryResponse inventoryResponse = null;
        try {
            Utils.addCMRequest(inventoryRequest, transactionLog);
            List<InventoryUpdate> inventoryUpdateRequests = buildInventoryUpdateRequest(inventoryRequest);
            for (InventoryUpdate eachInventoryUpdate : inventoryUpdateRequests) {
                String jsonString = MarshalUnmarshalUtils.serialize(eachInventoryUpdate);
                log.info("update inventory request:: " + jsonString);
                Utils.addOTARequest(jsonString, transactionLog);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(jsonString, httpHeaders);
                ResponseEntity<String> responseEntity = restTemplate
                        .postForEntity(getUpdateInvUrl, entity, String.class);
                String responseJson = responseEntity.getBody();
                log.info("Response for update inventory....." + responseJson);
                Utils.addOTAResponse(responseJson, transactionLog);
                InventoryUpdateResponse inventoryUpdateResponse = MarshalUnmarshalUtils.deserialize(responseJson,InventoryUpdateResponse.class);
                inventoryResponse = buildInventoryResponse(inventoryUpdateResponse);
                Utils.addCMResponse(inventoryResponse, transactionLog);
            }

        } catch (Throwable throwable) {
            Utils.addOTAResponse(throwable, transactionLog);
            throw throwable;
        } finally {
            repository.save(transactionLog);
        }
        return inventoryResponse;
    }

    @Override
    public PriceResponse updatePrice(PriceRequest priceRequest) throws OccupancyNotSupportedException, Exception {
        TransactionLog transactionLog = new TransactionLog();
        Utils.addCommonData(priceRequest, transactionLog);
        RatePlanResponse ratePlanResponse = null;
        PriceResponse priceResponse=null;
        try {
            Utils.addCMRequest(priceRequest, transactionLog);
            for (UpdatePriceOTA updatePriceOTA : buildUpdatePriceRequests(priceRequest)) {
                String jsonString = MarshalUnmarshalUtils.serializeWithPropertyNaming(updatePriceOTA);
                log.info("update price request:: " + jsonString);
                Utils.addOTARequest(jsonString, transactionLog);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(jsonString, httpHeaders);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(getUpdatePriceUrl, entity, String.class);
                String responseJson = responseEntity.getBody();
                log.info("Response for update price....." + responseJson);
                Utils.addOTAResponse(responseJson, transactionLog);
                PriceUpdateResponse priceUpdateResponse = MarshalUnmarshalUtils.deserialize(responseJson, PriceUpdateResponse.class);
                priceResponse = buildPriceResponse(priceUpdateResponse);
                Utils.addCMResponse(priceResponse, transactionLog);
            }
        }catch (Throwable throwable) {
            Utils.addOTAResponse(throwable, transactionLog);
            throw throwable;
        } finally {
            repository.save(transactionLog);
        }
        return priceResponse;
    }

    @Override
    public InventoryResponse updateRestriction(RestrictionRequest restrictionRequest) throws Exception {
        return null;
    }

    /*
    Because common format has support for single room per request as of now
     */
    private List<InventoryUpdate> buildInventoryUpdateRequest(InventoryRequest inventoryRequest) {
        List<InventoryUpdate> inventoryUpdates = new ArrayList<InventoryUpdate>();
        for (InventoryData eachInventoryData : inventoryRequest.getData()) {
            InventoryUpdate inventoryUpdate = new InventoryUpdate();
            inventoryUpdates.add(inventoryUpdate);
            Auth auth = new Auth();
            inventoryUpdate.setAuth(auth);
            auth.setKey(apiKey);
            Data data = new Data();
            inventoryUpdate.setData(data);
            data.setPropertyId(inventoryRequest.getHotelId());
            data.setRoomId(eachInventoryData.getRoomId());
            List<com.axisrooms.mistay.generated.updateInventory.Inventory> inventories = new ArrayList<>();
            data.setInventory(inventories);
            for (Inventory eachInventory : eachInventoryData.getInventories()) {
                com.axisrooms.mistay.generated.updateInventory.Inventory inventory = new com.axisrooms.mistay.generated.updateInventory.Inventory();
                inventories.add(inventory);
                inventory.setEndDate(eachInventory.getEndDate());
                inventory.setStartDate(eachInventory.getStartDate());
                inventory.setFree(eachInventory.getInventory());
            }
        }
        return inventoryUpdates;
    }

    private InventoryResponse buildInventoryResponse(InventoryUpdateResponse inventoryUpdateResponse) {
        InventoryResponse inventoryResponse = new InventoryResponse();
        if (inventoryUpdateResponse.getStatus().equalsIgnoreCase("success")) {
            inventoryResponse.setMessage("success");
            inventoryResponse.setHttpStatusCode(HttpStatus.OK.value());
        } else {
            inventoryResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            inventoryResponse.setMessage(inventoryUpdateResponse.getMessage());
        }
        return inventoryResponse;
    }

    private PriceResponse buildPriceResponse(PriceUpdateResponse priceUpdateResponse) {
        PriceResponse priceResponse = new PriceResponse();
        if (priceUpdateResponse.getstatus().equalsIgnoreCase("success")) {
            priceResponse.setMessage("success");
            priceResponse.setHttpStatusCode(HttpStatus.OK.value());
        } else {
            priceResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            priceResponse.setMessage(priceUpdateResponse.getmessage());
        }
        return priceResponse;
    }

    private List<UpdatePriceOTA> buildUpdatePriceRequests(PriceRequest priceRequest)
            throws OccupancyNotSupportedException {
        List<UpdatePriceOTA> updatePriceOTAList = new ArrayList<>();
        String hotelId = priceRequest.getHotelId();
        for (PriceData eachPriceData : priceRequest.getData()) {
            for (RoomDetail roomDetail : eachPriceData.getRoomDetails()) {
                for (RatePlanDetail ratePlanDetail : roomDetail.getRatePlanDetails()) {
                    try {
                        for (com.axisrooms.mistay.bean.Rate eachRate : ratePlanDetail.getRates()) {
                            //multiple apis here
                            UpdatePriceOTA updatePriceOTA = new UpdatePriceOTA();
                            updatePriceOTAList.add(updatePriceOTA);
                            com.axisrooms.mistay.generated.updatePrice.Auth auth = new com.axisrooms.mistay.generated.updatePrice.Auth();
                            updatePriceOTA.setauth(auth);
                            auth.setkey(apiKey);
                            com.axisrooms.mistay.generated.updatePrice.Data data = new com.axisrooms.mistay.generated.updatePrice.Data();
                            updatePriceOTA.setdata(data);
                            data.setpropertyId(hotelId);
                            data.setroomId(roomDetail.getRoomId());
                            data.setrateplanId(ratePlanDetail.getRatePlanId());
                            List<Rate> rates = new ArrayList<>();
                            data.setrate(rates);
                            Map<String, String> priceMap = eachRate.getPrices();
                            Rate rate = new Rate();
                            rates.add(rate);
                            rate.setendDate(eachRate.getEndDate());
                            rate.setstartDate(eachRate.getStartDate());
                            for (String occupancy : priceMap.keySet()) {
                                rate.setPriceByOccupancyName(occupancy, priceMap.get(occupancy));
                            }
                        }
                    } catch (OccupancyNotSupportedException e) {
                        log.error("Exception while preparing price update api input:: " + e.getMessage());
                        throw e;
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw e;
                    }
                }
            }

        }
        return updatePriceOTAList;
    }

    private RatePlanResponse buildRatePlanResponse(RateUpdateResponse rateUpdateResponse) {
        RatePlanResponse ratePlanResponse = new RatePlanResponse();
        if (rateUpdateResponse.getStatus().equalsIgnoreCase("success")) {
            ratePlanResponse.setMessage("success");
            ratePlanResponse.setHttpStatusCode(HttpStatus.OK.value());
        } else {
            ratePlanResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            ratePlanResponse.setMessage(ratePlanResponse.getMessage());
        }
        return ratePlanResponse;
    }

    private ProductInfoRequest buildProductInfoRequest(String hotelId) {
        ProductInfoRequest productInfoRequest = new ProductInfoRequest();
        com.axisrooms.mistay.generated.productInfo.Auth auth = new com.axisrooms.mistay.generated.productInfo.Auth();
        productInfoRequest.setAuth(auth);
        auth.setKey(apiKey);
        productInfoRequest.setPropertyId(hotelId);
        return productInfoRequest;
    }

    private ProductInfoResponse getProductInfo(ProductInfoRequest productInfoRequest) throws Exception {
        String jsonRequest = MarshalUnmarshalUtils.serialize(productInfoRequest);
        log.info("Input request to fetch rooms: -> " + jsonRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(jsonRequest, httpHeaders);
        String response = response = restTemplate.postForObject(getRoomsUrl, entity, String.class);
//        String dummyData=readUsingBufferedReader("/home/tech/Desktop/otatestfile/getProductInfo.txt");
        log.info("Axisrooms ota getRooms Response=" + response);
        return MarshalUnmarshalUtils.deserialize(response, ProductInfoResponse.class);
    }

    /*
    axisagent-commonOta conversion
     */
    private RoomResponse buildRoomResponse(ProductInfoResponse productInfoResponse) throws Exception {
        RoomResponse roomResponse = new RoomResponse();
        if (Objects.nonNull(productInfoResponse)) {
            if (productInfoResponse.getStatus().equalsIgnoreCase("success")) {
                Set<Description> descriptions = new HashSet<>();
                for (Datum datum : productInfoResponse.getData()) {
                    Description description = new Description();
                    descriptions.add(description);
                    description.setId(datum.getId());
                    description.setName(datum.getName());
                }
                roomResponse.setDescriptions(descriptions);
                roomResponse.setMessage(SUCCESS);
                roomResponse.setHttpStatusCode(HttpStatus.OK.value());
            } else {
                roomResponse
                        .setMessage("request to fetch rooms failed from ota::  " + productInfoResponse.getMessage());
                roomResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } else {
            roomResponse.setMessage("Marshaling/Serialization Exception Occured.");
            roomResponse.setHttpStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
        }
        return roomResponse;
    }

    private RatePlanInfoRequest buildRatePlanInfoRequest(String hotelId, String roomId) {
        RatePlanInfoRequest ratePlanInfoRequest = new RatePlanInfoRequest();
        com.axisrooms.mistay.generated.RatePlanInfo.Auth auth = new com.axisrooms.mistay.generated.RatePlanInfo.Auth();
        ratePlanInfoRequest.setAuth(auth);
        auth.setKey(apiKey);
        ratePlanInfoRequest.setPropertyId(hotelId);
        ratePlanInfoRequest.setRoomId(roomId);
        return ratePlanInfoRequest;
    }

    private RatePlanInfoResponse getRatePlanInfo(RatePlanInfoRequest ratePlanInfoRequest) throws Exception {
        String jsonRequest = MarshalUnmarshalUtils.serialize(ratePlanInfoRequest);
        log.info("Input request to fetch rate plans: -> " + jsonRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(jsonRequest, httpHeaders);
        String response = "";
//        response = restTemplate.postForObject(getRoomsUrl, entity, String.class);  use this URL for getRooms
        response = restTemplate.postForObject(getProductInfoUrl, entity, String.class); //use this for getRatePlans
        log.info("Axisrooms ota get rate plans Response=" + response);
        return MarshalUnmarshalUtils.deserialize(response, RatePlanInfoResponse.class);
    }

    private RatePlanResponse buildRatePlansResponse(ProductInfoResponse ratePlanInfoResponse, String roomId){
        RatePlanResponse ratePlanResponse = new RatePlanResponse();
        if (Objects.nonNull(ratePlanInfoResponse)) {
            if (ratePlanInfoResponse.getStatus().equalsIgnoreCase("success")) {
                ratePlanResponse.setHttpStatusCode(HttpStatus.OK.value());
                ratePlanResponse.setMessage("success");
                List<RatePlanDescription> ratePlanDescriptions = new ArrayList<>();
                ratePlanResponse.setRatePlanDescriptions(ratePlanDescriptions);
                RatePlanDescription ratePlanDescription = new RatePlanDescription();
                ratePlanDescriptions.add(ratePlanDescription);
                ratePlanDescription.setRoomId(roomId);
                List<RatePlanConfiguration> ratePlanConfigurations = new ArrayList<>();
                ratePlanDescription.setConfigurations(ratePlanConfigurations);
                for (com.axisrooms.mistay.generated.productInfo.Datum datum : ratePlanInfoResponse.getData()) {
                    RatePlanConfiguration ratePlanConfiguration = new RatePlanConfiguration();
                    ratePlanConfigurations.add(ratePlanConfiguration);
//                    if (datum.getCommissionPerc()!=null){
//                        ratePlanConfiguration.setCommission(datum.getCommissionPerc());
//                    }
//                    if (datum.getRateplanId()!=null) {
                        ratePlanConfiguration.setRatePlanId(datum.getId());
//                    }
//                    if (datum.getRatePlanName()!=null){
                        ratePlanConfiguration.setRatePlanName(datum.getName());
//                    }
//                    if (datum.getTaxPerc()!=null) {
//                        ratePlanConfiguration.setTax(datum.getTaxPerc());
//                    }
//                    if (datum.getValidity()!=null){
//                        Validity validity = datum.getValidity();
//                        RatePlanValidity ratePlanValidity = new RatePlanValidity();
//                        ratePlanValidity.setStartDate(validity.getStartDate());
//                        ratePlanValidity.setEndDate(validity.getEndDate());
//                        ratePlanConfiguration.setValidityList(new ArrayList<RatePlanValidity>(Arrays.asList(ratePlanValidity)));
//                    }

                    List<String> occupancies = new ArrayList<>();
                    ratePlanDescription.setOccupancies(occupancies);
                    //occupancy (rate plan level or room level?)
//                    for (String occupancy : datum.getOccupancy()) {
                        occupancies.add("single");
                        occupancies.add("double");
//                    }
                }
            } else {
                ratePlanResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                ratePlanResponse.setMessage("External api call failed " + ratePlanInfoResponse.getMessage());
            }
        } else {
            ratePlanResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            ratePlanResponse.setMessage("Marshalling error occurred");
        }
        return ratePlanResponse;
    }

}