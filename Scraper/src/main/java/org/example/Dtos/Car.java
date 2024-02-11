package org.example.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Car {
   long id;
   long orderId;

   @JsonProperty ("carTitle")
   private String  carName;

   @JsonProperty ("carThumbUrl")
   private String carImageUrl;
   private String friendlyCarPath;

   private String distance;
   private String Rating;
   private String ratingCount;
   @JsonProperty("humanPrice")
   private String Price;
   @JsonProperty ("shiftedLatitude")
   private String Latitude;
   @JsonProperty ("shiftedLongitude")
   private String Longitude;
//    isPicked: false,
//    icon: "open",
//    instantBookable: true,
//    carIsOpen: true,
   //    probaLevel: "good",
  //    isPricePerDay: false,
//    friendlyCarPath: "/car-rental/brooklyn/audi-q7-1389357",
//    carPreviewUrl: "/Car/1389357/preview?distance=600&duration=For+2+days%2C+30+min&ends_at=2024-02-17+07%3A30%3A00+-0500&pick_cta=true&price_placeholder=true&program=getaround&searched_address%5Blatitude%5D=40.6665&searched_address%5Blongitude%5D=-73.8832&starts_at=2024-02-15+07%3A00%3A00+-0500",


}
/*
Car Name
//Car Image Url
//Distance
//Rating
//Rating Count
//Price
//Car Location Latitude (Must be scraped)
//Car Location Longitude (Must be scraped)
//
 */