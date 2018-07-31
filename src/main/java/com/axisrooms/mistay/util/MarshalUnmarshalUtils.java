package com.axisrooms.mistay.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class MarshalUnmarshalUtils {
    private static JAXBContext jaxbContext;

    static {
        try {
            jaxbContext = JAXBContext.newInstance();
        } catch (JAXBException e) {
            log.error("Could not create a new JAXBContext", e);
        }
    }

    private static Marshaller createMarshaller(JAXBContext jaxbContext) throws JAXBException {
        Marshaller marshaller = null;
        try {
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch (JAXBException e) {
            //log.error("Error during creating marshaller", e);
            throw e;
        }
        return marshaller;
    }

    private static Unmarshaller createUnmarshaller(JAXBContext jaxbContext) throws JAXBException {
        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            //log.error("Error during creating unmarshaller", e);
            throw e;
        }
        return unmarshaller;
    }

    public static <T> String marshal(T t) throws JAXBException {
        OutputStream stream = new ByteArrayOutputStream();
        try {
            Marshaller marshaller = createMarshaller(jaxbContext);
            marshaller.marshal(t, stream);
        } catch (JAXBException e) {
            //log.error("Error during marshalling for class "+ t.getClass().getName(), e);
            throw e;
        }
        return stream.toString();
    }

    public static <T> T unmarshal(String data) throws JAXBException {
        T t = null;
        try {
            Unmarshaller unmarshaller = createUnmarshaller(jaxbContext);
            t = (T) unmarshaller.unmarshal(new StringReader(data));
        } catch (JAXBException e) {
            //log.error("Error during unmarshalling", e);
            throw e;
        }
        return t;
    }

    public static <T> String serialize(T t) throws JsonProcessingException {
        String result = null;
        try {
            if (t != null) {
                ObjectMapper objectMapper = ObjectMapperSingleton.get();
                JavaTimeModule javaTimeModule = getJavaTimeModule();
                objectMapper.registerModule(javaTimeModule);
                result = objectMapper.writeValueAsString(t);
            }
        } catch (JsonProcessingException e) {
            log.error("Could not serialize {} to JSON string.", t.getClass().getName(), e);
            throw e;
        }
        return result;
    }

    public static <T> String serializeWithPropertyNaming(T t) throws JsonProcessingException {
        String result = null;
        try {
            if (t != null) {
                ObjectMapper objectMapper = ObjectMapperSingleton.get();
                objectMapper.setPropertyNamingStrategy(new PropertyCaseNamingStrategy());
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                StringWriter sw = new StringWriter();
                JavaTimeModule javaTimeModule = getJavaTimeModule();
                objectMapper.registerModule(javaTimeModule);
                objectMapper.writeValue(sw, t);
                result=sw.toString();
            }
        } catch (JsonProcessingException e) {
            log.error("Could not serialize {} to JSON string.", t.getClass().getName(), e);
            throw e;
        }catch (Exception e){

        }
        return result;
    }

    public static <T> T deserialize(String data, Class<T> clazz) {
        T t = null;
        try {
            if (!StringUtils.isEmpty(data)) {
                ObjectMapper objectMapper = ObjectMapperSingleton.get();
                JavaTimeModule javaTimeModule = getJavaTimeModule();
                objectMapper.registerModule(javaTimeModule);
                objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                t = objectMapper.readValue(data, clazz);
            }
        } catch (IOException e) {
            log.error("Could not deserialize JSON string to class.", e);
            if (t != null) {
                log.error("Classname %s", t.getClass().getName());
            }
        }
        return t;
    }

    private static JavaTimeModule getJavaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return javaTimeModule;
    }
}
