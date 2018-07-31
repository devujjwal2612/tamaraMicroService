package com.axisrooms.mistay.response;

import lombok.Data;

import java.util.Set;

@Data
public class RoomResponse {
    private String message;
    private int httpStatusCode;
    private Set<Description> descriptions;
}