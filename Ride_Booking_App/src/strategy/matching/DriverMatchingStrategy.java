package strategy.matching;

import model.Driver;
import model.Location;

import java.util.List;

public interface DriverMatchingStrategy {
    List<Driver> findMatchingDrivers(Location pickup, List<Driver> candidates, int maxResults);
}
