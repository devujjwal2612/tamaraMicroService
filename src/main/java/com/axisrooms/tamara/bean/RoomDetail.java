package com.axisrooms.tamara.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomDetail {
    private String roomId;
    private List<RatePlanDetail> ratePlanDetails;
    private List<RatePlan> ratePlans;
}
