package com.axisrooms.mistay.request;

import com.axisrooms.mistay.bean.PriceData;
import com.axisrooms.mistay.request.validation.ValidPriceRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@ValidPriceRequest
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceRequest {
    private String          channelId;
    private String          token;
    private String          arcRequestId;
    private String          hotelId;
    private List<PriceData> data;
}
