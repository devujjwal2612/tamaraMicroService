package com.axisrooms.tamara.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperSingleton {
    private ObjectMapperSingleton() {

    }

    public static ObjectMapper get() {
        return LazyLoader.OBJECT_MAPPER;
    }

    private static class LazyLoader {
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    }
}
