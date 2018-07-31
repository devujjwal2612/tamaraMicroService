package com.axisrooms.mistay.request;

import com.axisrooms.mistay.bean.RestrictionData;
import com.axisrooms.mistay.request.validation.ValidIRestrictionRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@ValidIRestrictionRequest
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestrictionRequest {
    private String                channelId;
    private String                token;
    private String                arcRequestId;
    private String                hotelId;
    private List<RestrictionData> data;
}
