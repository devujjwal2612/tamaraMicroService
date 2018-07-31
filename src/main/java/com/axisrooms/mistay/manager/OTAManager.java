package com.axisrooms.mistay.manager;

import com.axisrooms.mistay.request.InventoryRequest;
import com.axisrooms.mistay.request.PriceRequest;
import com.axisrooms.mistay.request.RestrictionRequest;
import com.axisrooms.mistay.response.InventoryResponse;
import com.axisrooms.mistay.response.PriceResponse;
import com.axisrooms.mistay.response.RatePlanResponse;
import com.axisrooms.mistay.response.RoomResponse;
import com.axisrooms.mistay.util.OccupancyNotSupportedException;
import org.springframework.stereotype.Service;

@Service
public interface OTAManager {
    RoomResponse getRoomList(String hotelId) throws Exception;

    RatePlanResponse getRatePlans(String hotelId, String roomId) throws Exception;

    InventoryResponse updateInventory(InventoryRequest inventoryRequest) throws Exception;

    PriceResponse updatePrice(PriceRequest priceRequest) throws OccupancyNotSupportedException,Exception;

    InventoryResponse updateRestriction(RestrictionRequest restrictionRequest) throws Exception;
}
