# WEB SCRAPER
# GetAround Web Scraper

## Overview
This project aims to develop a web scraper capable of extracting specific information from the GetAround website, focusing on vehicle listings in the New York area. The scraper allows users to retrieve details about available vehicles based on location, date, and time inputs.

## Problem Statement
The objective of this assignment is to develop a web scraper to extract vehicle details from the GetAround website, specifically targeting the New York area. The scraper should handle inputs for location, start date/time, and end date/time, and extract relevant information such as car name, image URL, distance, rating, rating count, price, latitude, and longitude for each vehicle.

## Features
- **Scrape Vehicle Listings:** Extracts vehicle details such as car name, image URL, distance, rating, rating count, price, latitude, and longitude from GetAround.
- **Input Validation:** Validates input dates, times, and formats to ensure accurate scraping.
- **Proxy Setup:** Utilizes proxy servers for enhanced security and anonymity during HTTP requests.
- **Asynchronous Processing:** Uses asynchronous processing to improve performance and efficiency when parsing HTML pages and fetching data.

## Technologies Used
1. **Spring Boot Starter Web:** Used for building web applications and exposing REST endpoints.
2. **Jsoup:** Employed for HTML parsing to extract data from web pages.
3. **Java Spring RestTemplate:** Utilized for making HTTP requests to interact with the GetAround website's APIs.
4. **Lombok:** Used for reducing boilerplate code by automatically generating getters, setters, constructors, etc.
5. **Slf4j:** Integrated for logging purposes to facilitate debugging and monitoring.
6. **Spring Boot Starter Log4j2:** Configured for logging with Log4j2, providing flexible logging options.
7. **Rotating Proxies:** Implemented to enhance security and anonymity during HTTP requests, ensuring robustness against potential blocking or throttling from the GetAround website.
8. **Asynchronous Processing:** Utilized asynchronous processing techniques to improve performance and efficiency when parsing HTML pages and fetching data.

## Validations
- **startDate/endDate:** Format must be yyyy-MM-dd.
- **startTime/endTime:** Format must be HH:mm (24-hour format).
- **endDate & endTime > startDate & startTime > current Date And Time**

## Response
- **Success:** HTTP status code 200.
- **Body:** List of Car objects.

## Approach
- **API Endpoint:** Exposed an API for users to fetch available vehicle details based on location, date, and time inputs.
- **Proxy Rotation:** Implemented rotating proxies to bypass potential blocking or throttling from the GetAround website. Free proxies are fetched every 3 minutes from a specified URL.
- **Scraping Logic:** Utilized two main APIs for data extraction: one for fetching the list of available vehicles and another for retrieving detailed information about each vehicle.

## Directory Structure
- **Config:** Contains configuration classes for Spring Boot and bean definitions.
- **Controller:** Includes the controller class defining the REST endpoint for scraping GetAround data.
- **DTOs:** Data Transfer Objects representing the structure of the extracted data.
- **Exception:** Custom exception class for handling invalid input.
- **OutBound:** Service class for making outbound calls to the GetAround API.
- **Service:** Service interfaces and their implementations for scraping GetAround data.
- **Util:** Utility classes for date/time validation and other functionalities.
- **SpringBootApplicationClass:** Main class to run the Spring Boot application.

## Dependencies
- Spring Boot Starter Web: For building web applications.
- Jsoup: For HTML parsing.
- Java Spring RestTemplate: For making HTTP requests.
- Lombok: For reducing boilerplate code.
- Slf4j: For logging.
- Spring Boot Starter Log4j2: For logging configuration with Log4j2.

## Documentation
For detailed documentation of the API usage, validations, and response format, refer to the sections above.

## Improvements and Optimizations
- **Optimized Scraping Logic:** The code is optimized for scalability and performance using multithreading, proxy rotation, and other techniques.
- **Exception Handling:** Implemented robust error handling to handle various scenarios, such as invalid input and network errors.
- **Logging and Comments:** Consistent logging and comprehensive comments are added throughout the codebase for better understandability and debugging.


## Usage
### API Endpoint
To fetch available vehicle details, use the following API endpoint:
```bash
curl --location 'localhost:8080/scrape/getAround/?startDate=2024-02-15&startTime=07%3A30&endDate=2024-02-19&endTime=07%3A00'
