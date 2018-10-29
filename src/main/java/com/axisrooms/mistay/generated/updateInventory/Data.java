
package com.axisrooms.mistay.generated.updateInventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    @JsonProperty("inventory")
    private List<Inventory> inventory;
    @JsonProperty("propertyId")
    private String propertyId;
    @JsonProperty("roomId")
    private String roomId;
}
