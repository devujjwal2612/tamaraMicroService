
package com.axisrooms.tamara.generated.updatePrice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@lombok.Data
public class UpdatePriceOTA {
    @JsonProperty("auth")
    private Auth auth;
    @JsonProperty("data")
    private Data data;

    public Auth getauth() {
        return auth;
    }

    public void setauth(Auth auth) {
        this.auth = auth;
    }

    public Data getdata() {
        return data;
    }

    public void setdata(Data data) {
        this.data = data;
    }
}
