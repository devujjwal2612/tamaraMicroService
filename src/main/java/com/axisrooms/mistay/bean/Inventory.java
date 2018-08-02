package com.axisrooms.mistay.bean;

import com.axisrooms.mistay.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

@Data
public class Inventory {
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDate startDate;

    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDate endDate;
    private Integer inventory;
    private String ratePlanId;
    private List<Period> closedDates;
}
