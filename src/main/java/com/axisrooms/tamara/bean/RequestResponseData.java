package com.axisrooms.tamara.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RequestResponseData<T, E> {
    private T request;

    private E response;

    private Instant requestTime;

    private Instant responseTime;
}
