package org.example.Controller;

import lombok.extern.slf4j.Slf4j;
import org.example.Dtos.Car;
import org.example.Service.GetAroundScrapeService;
import org.example.Util.DateTimeValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController("/")
public class GetAroundScrapeController {

    @Autowired
    private GetAroundScrapeService getAroundScrapeService;

    @GetMapping("/scrape/getAround")
    public ResponseEntity<List<Car>> getAllVehicleDetails(@RequestParam("startDate") String startDate,
                                                  @RequestParam("startTime") String startTime,
                                                  @RequestParam("endDate") String endDate,
                                                  @RequestParam("endTime") String endTime) throws IOException {
        log.info("In getAllVehicleDetails. startDate {} startTime {} endDate {} endTime {}",
                startDate, startTime,endDate,endTime);
        DateTimeValidationUtils.validateStartAndEndDate(startDate,startTime,endDate,endTime);

        List<Car> cars = getAroundScrapeService.getAllVehiclesAvailable(
                startDate,startTime,
                endDate,endTime
        );

        return ResponseEntity.ok(cars);
    }

}
