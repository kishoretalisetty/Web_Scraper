package org.example.Service;

import org.example.Dtos.Car;

import java.io.IOException;
import java.util.List;

public interface GetAroundScrapeService {
    public List<Car> getAllVehiclesAvailable(String startDate,
                                             String startTime, String endDate, String endTime) throws IOException;
}
