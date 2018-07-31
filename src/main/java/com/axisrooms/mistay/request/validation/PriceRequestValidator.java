package com.axisrooms.mistay.request.validation;

import com.axisrooms.mistay.bean.Rate;
import com.axisrooms.mistay.bean.RatePlanDetail;
import com.axisrooms.mistay.bean.RoomDetail;
import com.axisrooms.mistay.request.PriceRequest;
import com.axisrooms.mistay.util.Utils;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class PriceRequestValidator implements ConstraintValidator<ValidPriceRequest, PriceRequest> {
    @Value("${microservice.communication.token}")
    private String acceptedToken;

    @Override
    public void initialize(ValidPriceRequest constraintAnnotation) {

    }

    @Override
    public boolean isValid(PriceRequest request, ConstraintValidatorContext context) {
        //todo
        //preconditions validations
        boolean result = true;
        try {
            Preconditions.checkArgument(request != null, "Request cannot be null");
            String token = request.getToken();
            String hotelId = request.getHotelId();
            Utils.isValid(token, hotelId, acceptedToken);
            Preconditions.checkArgument(!StringUtils.isEmpty(request.getArcRequestId()), "ArcRequestId cannot be null or empty");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(request.getData()), "Data cannot be null or empty");
            List<RoomDetail> roomDetailList = request.getData().get(0).getRoomDetails();
            Preconditions.checkArgument(!CollectionUtils.isEmpty(roomDetailList),"RoomDetails cannot be null or empty");
            for(RoomDetail roomDetail : roomDetailList){
                Preconditions.checkArgument(!StringUtils.isEmpty(roomDetail.getRoomId()), "RoomId cannot be null or empty");
                List<RatePlanDetail> ratePlanDetailList = roomDetail.getRatePlanDetails();
                Preconditions.checkArgument(!CollectionUtils.isEmpty(ratePlanDetailList),"RatePlanDetails cannot be null or empty");
                for(RatePlanDetail ratePlanDetail : ratePlanDetailList){
                    Preconditions.checkArgument(!StringUtils.isEmpty(ratePlanDetail.getRatePlanId()), "RatePlanId cannot be null or empty");
                    List<Rate> rateList = ratePlanDetail.getRates();
                    Preconditions.checkArgument(!CollectionUtils.isEmpty(rateList),"Rates section cannot be null or empty");
                    for(Rate rate : rateList){
                        Utils.validateDates(rate.getStartDate(), rate.getEndDate());
                    }
                }
            }
        } catch (Throwable throwable) {
            result = false;
            context.buildConstraintViolationWithTemplate(throwable.getMessage()).addConstraintViolation();
        }
        return result;
    }
}
