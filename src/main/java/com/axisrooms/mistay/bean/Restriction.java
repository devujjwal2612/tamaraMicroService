package com.axisrooms.mistay.bean;

import lombok.Data;

import java.util.List;

@Data
public class Restriction {
    private List<Period> periods;
    private String type;
    private String value;
}
