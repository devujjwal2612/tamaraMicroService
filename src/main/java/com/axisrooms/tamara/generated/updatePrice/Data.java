
package com.axisrooms.tamara.generated.updatePrice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@lombok.Data
public class Data {
    @JsonProperty("propertyId")
    private String propertyId;
    @JsonProperty("rate")
    private List<Rate> rate;
    @JsonProperty("rateplanId")
    private String rateplanId;
    @JsonProperty("roomId")
    private String roomId;

    public String getpropertyId() {
        return propertyId;
    }

    public void setpropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public List<Rate> getrate() {
        return rate;
    }

    public void setrate(List<Rate> rate) {
        this.rate = rate;
    }

    public String getrateplanId() {
        return rateplanId;
    }

    public void setrateplanId(String rateplanId) {
        this.rateplanId = rateplanId;
    }

    public String getroomId() {
        return roomId;
    }

    public void setroomId(String roomId) {
        this.roomId = roomId;
    }
}
