package model;

import java.time.LocalDateTime;

public class DateRange {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public DateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public boolean isWithin(LocalDateTime date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
