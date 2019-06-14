package com.axisrooms.tamara.response;

import lombok.Data;

import java.util.List;

@Data
public class RatePlanResponse {
    private String message;
    private int httpStatusCode;
    private List<RatePlanDescription> ratePlanDescriptions;
}
