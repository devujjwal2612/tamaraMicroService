package com.axisrooms.tamara.response;

import lombok.Data;

import java.util.List;

@Data
public class RatePlanConfiguration {
    private String ratePlanId;
    private String ratePlanName;
    private List<RatePlanValidity> validityList;
    private String commission;
    private String tax;
}
