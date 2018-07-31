
package com.axisrooms.mistay.generated.updateInventory;

import com.axisrooms.mistay.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@lombok.Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Inventory {
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDate endDate;
    private Integer      free;
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDate    startDate;
}
