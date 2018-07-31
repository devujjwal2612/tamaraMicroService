package com.axisrooms.mistay.bean;

import com.axisrooms.mistay.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rate {
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDate startDate;

    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDate endDate;
    private Integer tax;
    private Integer commission;
    private Map<String,String> prices;
}
