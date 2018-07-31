
package com.axisrooms.mistay.generated.updatePrice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Auth {
    private String key;

    public String getkey() {
        return key;
    }

    public void setkey(String key) {
        this.key = key;
    }
}
