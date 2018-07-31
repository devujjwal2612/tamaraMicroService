package com.axisrooms.mistay.response;

import com.axisrooms.mistay.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RatePlanValidity {
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDate startDate;
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDate endDate;
}
