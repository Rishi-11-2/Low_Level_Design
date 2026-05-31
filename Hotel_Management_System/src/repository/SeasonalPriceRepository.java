package repository;

import model.SeasonalPrice;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SeasonalPriceRepository {
    private final Map<String, SeasonalPrice> priceMap = new ConcurrentHashMap<>();

    private String getKey(String hotelId, String roomTypeId, long dateUtc) {
        return hotelId + "-" + roomTypeId + "-" + dateUtc;
    }

    public SeasonalPrice upsert(SeasonalPrice price) {
        String key = getKey(price.getHotelId(), price.getRoomTypeId(), price.getDateUtc());
        priceMap.put(key, price);
        return price;
    }

    public Optional<SeasonalPrice> findByKey(String hotelId, String roomTypeId, long dateUtc) {
        String key = getKey(hotelId, roomTypeId, dateUtc);
        return Optional.ofNullable(priceMap.get(key));
    }
}
