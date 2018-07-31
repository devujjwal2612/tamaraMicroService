package com.axisrooms.mistay.bean;

import com.axisrooms.mistay.enums.Day;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RatePlanDetail {
    private String          ratePlanId;
    private String          ratePlanName;
    private String          rateType;
    private String          currency;
    private List<Day>       days;
    private List<Rate>      rates;
    private List<Period>    closeOutDates;
    private List<Inventory> inventories;
    private Restriction     restrictions;
}
