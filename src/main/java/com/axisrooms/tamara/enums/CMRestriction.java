package com.axisrooms.tamara.enums;


import java.util.ArrayList;
import java.util.List;

public enum CMRestriction {
    STATUS(1, "Status"), MLOS(2, "Mlos"), COA(3, "COA"), COD(4, "COD");

    private final int id;
    private final String name;

    private static List<String> restrictionNames = new ArrayList<>();

    static {
        for(CMRestriction restriction : CMRestriction.values()){
            restrictionNames.add(restriction.getName());
        }
    }
    private CMRestriction(int idValue, String nameValue) {
        id = idValue;
        name = nameValue;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static List<String> getRestrictionNames() {
        return restrictionNames;
    }

    public static void setRestrictionNames(List<String> restrictionNames) {
        CMRestriction.restrictionNames = restrictionNames;
    }
}
