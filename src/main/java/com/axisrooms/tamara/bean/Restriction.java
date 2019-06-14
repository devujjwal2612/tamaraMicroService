package com.axisrooms.tamara.bean;

import lombok.Data;

import java.util.List;

@Data
public class Restriction {
    private List<Period> periods;
    private String type;
    private String value;
}
