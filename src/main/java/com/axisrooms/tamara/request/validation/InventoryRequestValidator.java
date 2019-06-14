package com.axisrooms.tamara.request.validation;

import com.axisrooms.tamara.bean.Inventory;
import com.axisrooms.tamara.bean.InventoryData;
import com.axisrooms.tamara.request.InventoryRequest;
import com.axisrooms.tamara.util.Utils;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Component
@Slf4j
public class InventoryRequestValidator implements ConstraintValidator<ValidInventoryRequest, InventoryRequest> {

    @Value("${microservice.communication.token}")
    private String acceptedToken;

    @Override
    public void initialize(ValidInventoryRequest constraintAnnotation) {

    }

    @Override
    public boolean isValid(InventoryRequest request, ConstraintValidatorContext context) {
        //todo
        //preconditions validations
        boolean result = true;
            try{
                Preconditions.checkArgument(request != null, "Request cannot be null");
                String token = request.getToken();
                String hotelId = request.getHotelId();
                Utils.isValid(token, hotelId, acceptedToken);
                List<InventoryData> data = request.getData();
                Preconditions.checkArgument(!CollectionUtils.isEmpty(data), "Data cannot be null or empty");
                Preconditions.checkArgument(!StringUtils.isEmpty(request.getArcRequestId()), "ArcRequestId cannot be null or empty");
                for (InventoryData inventoryData : data) {
                    Preconditions.checkArgument(!StringUtils.isEmpty(inventoryData.getRoomId()), "RoomId cannot be null or empty");
                    Preconditions.checkArgument(!CollectionUtils.isEmpty(inventoryData.getRatePlans()),"RatePlans cannot be null or empty for Wandertrails OTA");
                    Preconditions.checkArgument(!CollectionUtils.isEmpty(inventoryData.getInventories()), "Inventory block cannot be null or empty");
                    for (Inventory inventory : inventoryData.getInventories()) {
                        Utils.validateDates(inventory.getStartDate(), inventory.getEndDate());
                    }
                }
            }catch(Throwable throwable){
                log.error(throwable.getMessage());
                result = false;
                context.buildConstraintViolationWithTemplate(throwable.getMessage()).addConstraintViolation();
            }

        return result;
    }
}
