package com.axisrooms.mistay.request;

import com.axisrooms.mistay.bean.InventoryData;
import com.axisrooms.mistay.request.validation.ValidInventoryRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@ValidInventoryRequest
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryRequest {
    private String              channelId;
    private String              token;
    private String              arcRequestId;
    private String              hotelId;
    private List<InventoryData> data;
}
