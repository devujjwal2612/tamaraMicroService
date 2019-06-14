package com.axisrooms.tamara.enums;


import java.util.HashMap;
import java.util.Map;

public enum Occupancy {

    SINGLE(1, "single",10,1),
    DOUBLE(2, "double",10,2),
    TWIN(18, "twin",10,2),
    TRIPLE(3, "triple",10,3),
    QUAD(4, "quad",10,4),
    PENTA(20, "penta",10,5),
    HEXA(19, "hexa",10,6),
    HEPTA(21, "hepta",10,7),
    OCTA(22, "octa",10,8),
    NONA(26, "nona",10,9),
    DECA(27, "deca",10,10),
    EXTRA_BED(12, "extraBed",10,1),
    EXTRA_ADULT(5, "extraAdult",10,1),
    EXTRA_CHILD(11, "extraChild",8,1),
    EXTRA_ADULT2(14, "extraAdult2",10,1),
    EXTRA_CHILD2(16, "extraChild2",8,1),
    EXTRA_ADULT3(15, "extraAdult3",10,1),
    EXTRA_CHILD3(17, "extraChild3",8,1);

    private static Map<Integer, Occupancy> occupancyMap = new HashMap<>();

    static {
        for (Occupancy entity : Occupancy.values()) {
            occupancyMap.put(entity.getId(), entity);
        }
    }

    private final int id;
    private final String name;
    private final int ageQualifyingCode;
    private final int capacity;
    private Occupancy(int occId, String occName, int occAgeQualifyingCode, int capacity) {
        id = occId;
        name = occName;
        ageQualifyingCode = occAgeQualifyingCode;
        this.capacity = capacity;
    }

    public static Occupancy valueOf(int id) {
        return occupancyMap.get(id);
    }

    public int getCapacity(){return capacity;}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAgeQualifyingCode() {
        return ageQualifyingCode;
    }

    public static Map<Integer, Occupancy> getOccupancyMap() {
        return occupancyMap;
    }

    public static void setOccupancyMap(Map<Integer, Occupancy> occupancyMap) {
        Occupancy.occupancyMap = occupancyMap;
    }
}
