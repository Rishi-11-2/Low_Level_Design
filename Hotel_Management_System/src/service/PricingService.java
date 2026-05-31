package service;

import model.DateRange;
import model.NightlyPrice;
import model.RoomType;
import model.SeasonalPrice;
import repository.RoomTypeRepository;
import repository.SeasonalPriceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PricingService {
    private final SeasonalPriceRepository seasonalPriceRepository;
    private final RoomTypeRepository roomTypeRepository;

    public PricingService(SeasonalPriceRepository seasonalPriceRepository,
                          RoomTypeRepository roomTypeRepository) {
        this.seasonalPriceRepository = seasonalPriceRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    public List<NightlyPrice> rateStay(String hotelId, String roomTypeId, DateRange range) {
        List<NightlyPrice> prices = new ArrayList<>();
        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElse(null);
        if (roomType == null) {
            return prices;
        }

        long dayMs = 24 * 60 * 60 * 1000L;
        long start = range.getStartDateUtc();
        long end = range.getEndDateUtc();

        for (long date = start; date < end; date += dayMs) {
            Optional<SeasonalPrice> seasonal = seasonalPriceRepository.findByKey(hotelId, roomTypeId, date);
            long rate = seasonal.map(SeasonalPrice::getPriceMinor).orElse(roomType.getBasePriceMinor());
            prices.add(new NightlyPrice(date, rate));
        }

        return prices;
    }

    public long computeTotal(List<NightlyPrice> nightly) {
        return nightly.stream().mapToLong(NightlyPrice::getPriceMinor).sum();
    }

    public double computeAveragePricePerNight(List<NightlyPrice> nightly, int numberOfNights) {
        if (numberOfNights == 0) return 0.0;
        return (double) computeTotal(nightly) / numberOfNights;
    }
}
