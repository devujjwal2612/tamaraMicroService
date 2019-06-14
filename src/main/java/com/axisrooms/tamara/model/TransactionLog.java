package com.axisrooms.tamara.model;

import com.axisrooms.tamara.bean.RequestResponseData;
import com.axisrooms.tamara.enums.OTA;
import com.axisrooms.tamara.enums.Operation;
import com.axisrooms.tamara.enums.ServiceName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * This class is to store all the log information in a mongo db
 */
@Document(collection = "transactionLog")
@Data
public class TransactionLog {
    @Id
    private String id;

    @Indexed
    private String hotelId;

    @Indexed
    private String roomId;

    @Indexed
    private String ratePlanId;

    private RequestResponseData cmData;

    private RequestResponseData otaData;

    private Operation operation;

    @Indexed
    private OTA ota;

    private ServiceName service;

    @Indexed
    private LocalDate date;
}
