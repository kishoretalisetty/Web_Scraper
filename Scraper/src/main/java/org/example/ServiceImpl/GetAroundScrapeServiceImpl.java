package org.example.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.Dtos.Car;
import org.example.Dtos.RequestMapDto;
import org.example.OutBound.OutBoundApplicationCaller;
import org.example.Service.GetAroundScrapeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class GetAroundScrapeServiceImpl implements GetAroundScrapeService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProxySetup proxySetup;

    @Value("${getaround.baseurl}")
    private String getAroundClientBaseUrl;

    @Autowired
    private OutBoundApplicationCaller outBoundApplicationCaller;


    @Override
    public List<Car> getAllVehiclesAvailable(String startDate,
                                             String startTime,
                                             String endDate, String endTime) throws IOException {


        List<RequestMapDto> pageResponseList = outBoundApplicationCaller
                .getAllVehiclesAvailable(startDate,startTime,endDate,endTime);

        log.info("successfully fetched all vehicles data. size {}",pageResponseList.get(0).getTotal_count());

        List<Car> carsAvailable = new ArrayList<>();
        for(RequestMapDto resp : pageResponseList) {
            getTheData(resp);
            carsAvailable.addAll(resp.getCars());
        }


        log.info("response size : {}",carsAvailable.size());
        return carsAvailable;
    }

    public void getTheData(RequestMapDto requestMapDto) throws IOException {
        log.info("processing {}",requestMapDto.getHumanized_results_count());
        Map<Long, Car> carMap = new HashMap<>();

        try{
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (Car car : requestMapDto.getCars()) {
                carMap.put(car.getId(),car);
                futures.add(CompletableFuture.runAsync(()->parseLatAndLong(car)));
            }
            log.info("latitude and longitude parsing completed for {}",requestMapDto.getHumanized_results_count());

            Document document= Jsoup.parse(requestMapDto.getResults_html());
            Elements elements=document.select(".pick_result");

            for (Element el : elements) {
                Long carId = Long.valueOf(el.select("div > a").attr("data-car-id"));
                Car car = carMap.get(carId);
                log.info("parsing for car {}",car.getCarName()+"-"+car.getId());

                String distance = el.select(".car-distance").attr("title");
                String rating = el.select(".cobalt-rating__label").text();
                if("No reviews".equals(rating) || "New listing".equals(rating)) {
                    car.setRating(rating);
                    continue;
                }
                else {
                    Element ratingEl = el.select(".cobalt-rating__label").first();
//                    System.out.println(ratingEl);
                    String ratingValue = ratingEl.select("div > span").get(0).text();
                    String ratingCount = ratingEl.select("div > span").get(1).text();
                    car.setRating(ratingValue);
                    car.setRatingCount(ratingCount);
                }
                car.setDistance(distance);
            }

            for (CompletableFuture<Void> f :futures) {
                f.get();
            }

        } catch (ExecutionException | InterruptedException e) {
            log.error("exception while parsing {}",requestMapDto.getHumanized_results_count(),e);
            e.printStackTrace();
        } catch (Exception e){
            log.error("exception while parsing {}",requestMapDto.getHumanized_results_count(),e);
            e.printStackTrace();
            throw e;
        }
        log.info("successfully parsed {}",requestMapDto.getHumanized_results_count());
    }

    public void parseLatAndLong(Car car){
        parseLatAndLong(car,5);
    }

    public void parseLatAndLong(Car car, int retryCount){
//        log.info("pulling latitude and longitude details for car : {}",car.getCarName()+"-"+car.getId());
        if(retryCount == 0){
            log.info("car not processed {}",car.getId());
            return;
        }
        try {
            String ipAddress = proxySetup.getIp();
            Document doc = Jsoup
                    .connect(getAroundClientBaseUrl + car.getFriendlyCarPath())
                    .proxy(ipAddress.split(":")[0],
                            Integer.parseInt(ipAddress.split(":")[1]))
                    .get();

            String lat = doc.select("#drivy_orders_order_form_latitude").attr("value");
            String longitude = doc.select("#drivy_orders_order_form_longitude").attr("value");

            car.setLatitude(lat);
            car.setLongitude(longitude);
            log.info("successfully processed car {}",car.getId());
        } catch (IOException e) {
            log.info("Exception encountered. {}",e.getMessage());
            parseLatAndLong(car,retryCount-1);
        }

//        log.info("successfully pulled lat and long details for car {}",car.getCarName()+"-"+car.getId());
    }
}
