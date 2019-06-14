package com.axisrooms.tamara.manager;

import com.axisrooms.tamara.request.InventoryRequest;
import com.axisrooms.tamara.request.PriceRequest;
import com.axisrooms.tamara.request.RestrictionRequest;
import com.axisrooms.tamara.response.InventoryResponse;
import com.axisrooms.tamara.response.PriceResponse;
import com.axisrooms.tamara.response.RatePlanResponse;
import com.axisrooms.tamara.response.RoomResponse;
import com.axisrooms.tamara.util.OccupancyNotSupportedException;
import org.springframework.stereotype.Service;

@Service
public interface OTAManager {
    RoomResponse getRoomList(String hotelId) throws Exception;

    RatePlanResponse getRatePlans(String hotelId, String roomId) throws Exception;

    InventoryResponse updateInventory(InventoryRequest inventoryRequest) throws Exception;

    PriceResponse updatePrice(PriceRequest priceRequest) throws OccupancyNotSupportedException,Exception;

    InventoryResponse updateRestriction(RestrictionRequest restrictionRequest) throws Exception;
}
