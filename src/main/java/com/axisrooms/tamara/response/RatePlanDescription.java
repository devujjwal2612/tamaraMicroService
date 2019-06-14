package com.axisrooms.tamara.response;

import lombok.Data;

import java.util.List;

@Data
public class RatePlanDescription {
    private String                      roomId;
    private String                      currency;
    private List<String>             occupancies;
    private List<RatePlanConfiguration> configurations;
}
