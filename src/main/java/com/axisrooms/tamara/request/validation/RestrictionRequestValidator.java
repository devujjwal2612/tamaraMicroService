package com.axisrooms.tamara.request.validation;

import com.axisrooms.tamara.bean.Period;
import com.axisrooms.tamara.bean.RatePlanDetail;
import com.axisrooms.tamara.bean.RestrictionData;
import com.axisrooms.tamara.bean.RoomDetail;
import com.axisrooms.tamara.enums.CMRestriction;
import com.axisrooms.tamara.request.RestrictionRequest;
import com.axisrooms.tamara.util.Utils;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

@Slf4j
public class RestrictionRequestValidator implements ConstraintValidator<ValidIRestrictionRequest, RestrictionRequest> {

    @Value("${microservice.communication.token}")
    private String acceptedToken;

    @Override
    public void initialize(ValidIRestrictionRequest constraintAnnotation) {

    }

    @Override
    public boolean isValid(RestrictionRequest request, ConstraintValidatorContext context) {
        //todo
        //add preconditions validations here
        boolean result = true;
        try {
            Preconditions.checkArgument(request != null, "Request cannot be null");
            String token = request.getToken();
            String hotelId = request.getHotelId();
            Utils.isValid(token, hotelId, acceptedToken);
            List<RestrictionData> data = request.getData();
            Preconditions.checkArgument(!CollectionUtils.isEmpty(data), "Data cannot be null or empty");
            Preconditions.checkArgument(!StringUtils.isEmpty(request.getArcRequestId()), "ArcRequestId cannot be null or empty");
            for (RestrictionData restrictionData : data) {
                List<RoomDetail> roomDetailList = restrictionData.getRoomDetails();
                Preconditions.checkArgument(!CollectionUtils.isEmpty(roomDetailList), "RoomDetails block cannot be null or empty");
                for(RoomDetail roomDetail : roomDetailList){
                    Preconditions.checkArgument(!StringUtils.isEmpty(roomDetail.getRoomId()), "RoomId cannot be null or empty");
                    Preconditions.checkArgument(!CollectionUtils.isEmpty(roomDetail.getRatePlans()),"RatePlans cannot be null or empty for Traveloka OTA");
                    List<RatePlanDetail> ratePlanDetailList = roomDetail.getRatePlanDetails();
                    Preconditions.checkArgument(!CollectionUtils.isEmpty(ratePlanDetailList), "RatePlanDetails block cannot be null or empty");
                    for(RatePlanDetail ratePlanDetail : ratePlanDetailList){
                        Preconditions.checkArgument(!StringUtils.isEmpty(ratePlanDetail.getRatePlanId()), "RatePlanId cannot be null or empty");
                        Preconditions.checkArgument(Objects.nonNull(ratePlanDetail.getRestrictions()),"Restriction block Cannot be empty/null");
                        Preconditions.checkArgument(!StringUtils.isEmpty(ratePlanDetail.getRestrictions().getType()), "Restriction type cannot be null or empty");
                        Preconditions.checkArgument(CMRestriction.getRestrictionNames().contains(ratePlanDetail.getRestrictions().getType()), "Restriction type should be any of these, "+CMRestriction.getRestrictionNames().toString());
                        Preconditions.checkArgument(!CollectionUtils.isEmpty(ratePlanDetail.getRestrictions().getPeriods()), "Restriction Periods block cannot be null or empty");
                        for(Period period : ratePlanDetail.getRestrictions().getPeriods()){
                            Utils.validateDates(period.getStartDate(), period.getEndDate());
                        }
                    }
                }
            }
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
            result = false;
            context.buildConstraintViolationWithTemplate(throwable.getMessage()).addConstraintViolation();
        }
        return result;
    }
}
