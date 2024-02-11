package org.example.OutBound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.Dtos.RequestMapDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class OutBoundApplicationCaller {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${getaround.search.vehicles.baseurl}")
    private String searchAllVehiclesBaseUrl;

    public List<RequestMapDto> getAllVehiclesAvailable(String startDate, String startTime,
                                                       String endDate, String endTime ) {
        List<RequestMapDto> listOfPageResponses = new ArrayList<>();

        // find Total Count
        RequestMapDto requestMapDto = getAllVehiclesAvailable(startDate,startTime,
                endDate,endTime,1);
        if(requestMapDto == null )  return listOfPageResponses;

        // find Number of pages
        int numOfPages = getNumberOfPages(requestMapDto.getTotal_count(),40);

        // traverse each page
        listOfPageResponses.add(requestMapDto);
        for (int i =1; i<= numOfPages; i++) {
            requestMapDto = getAllVehiclesAvailable(startDate,startTime,
                    endDate,endTime,i+1);
            listOfPageResponses.add(requestMapDto);
        }

        return listOfPageResponses;
    }

    public int getNumberOfPages(int totalCount, int baseCount) {
        int numOfPages = totalCount / baseCount;
        if(totalCount%baseCount == 0) numOfPages--;
        log.info("Total Count : {} PerPageCount : {} Number of pages : {}",totalCount,baseCount,numOfPages);
        return numOfPages;
    }

    public RequestMapDto getAllVehiclesAvailable(String startDate, String startTime,
                                          String endDate, String endTime, int page ) {
        log.info("calling api {} with startDate {} startTime {} endDate {} endTime {} page {}",
                searchAllVehiclesBaseUrl,startDate,startTime,endDate,endTime, page);

        Map<String,String> uriVariables = new HashMap<>();
        uriVariables.put("address","East New York, Brooklyn, NY");
        uriVariables.put("address_source","poi");
        uriVariables.put("country_scope","US");
        uriVariables.put("display_view","list");
        uriVariables.put("latitude","40.6665");
        uriVariables.put("longitude","-73.8832");
        uriVariables.put("picked_car_ids","EMPTY");
        uriVariables.put("poi_id","2631");
        uriVariables.put("program","getaround");
        uriVariables.put("view_mode","list");
        uriVariables.put("startDate",startDate);
        uriVariables.put("startTime",startTime);
        uriVariables.put("endDate",endDate);
        uriVariables.put("endTime",endTime);
        uriVariables.put("page", String.valueOf(page));
        RequestMapDto requestMapDto = null;
        try {
            requestMapDto = restTemplate
                    .getForEntity(searchAllVehiclesBaseUrl, RequestMapDto.class,uriVariables)
                    .getBody();

            assert requestMapDto != null;
            log.info("Vehicle data successfully fetched. totalCount : {} currentFetchCount {}",
                    requestMapDto.getTotal_count(),requestMapDto.getCars().size());

        } catch (Exception exception) {
            log.error("exception occured while fetching vehicle details ",exception);
            throw exception;
        }
        return requestMapDto;
    }

}
