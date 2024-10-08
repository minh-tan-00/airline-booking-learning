package com.airline.airline_booking.service.Impl;

import com.airline.airline_booking.model.Airport;
import com.airline.airline_booking.service.AirportService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AirportServiceImpl implements AirportService {
    private static final Logger logger = LoggerFactory.getLogger(AirportServiceImpl.class);

    private final String AIRPORT_CACHE_NAME = "airports";
    private final int CACHE_EXPIRED_SECONDS = 60;

    private final Cache<String, List<Airport>> airportCache;
    private final List<Airport> airportList;

    public AirportServiceImpl() {
        logger.info("Initialize Guava Cache with a TTL of 60 seconds");

        // Initialize Guava Cache with a TTL of 60 seconds
        airportCache = CacheBuilder.newBuilder()
                .expireAfterWrite(CACHE_EXPIRED_SECONDS, TimeUnit.SECONDS)
                .build();

        // Prepopulate airport list
        airportList = new ArrayList<>();
        airportList.add(new Airport("SGN", "Ho Chi Minh City Airport"));
        airportList.add(new Airport("HAN", "Hanoi Airport"));
        airportList.add(new Airport("DAD", "Da Nang Airport"));
        airportList.add(new Airport("CXR", "Nha Trang Cam Ranh Airport"));
        airportList.add(new Airport("PQC", "Phu Quoc Airport"));
        airportList.add(new Airport("HPH", "Haiphong Airport"));
    }

    @Override
    public List<Airport> getAirport() {
        // Check if the list of airports is present in cache
        List<Airport> cachedAirports = airportCache.getIfPresent(AIRPORT_CACHE_NAME);

        if (cachedAirports == null) {
            logger.info("Cache miss: Fetching airport data and caching it.");

            // Simulate slow data fetch and cache the result
            simulateSlowService();
            airportCache.put(AIRPORT_CACHE_NAME, airportList);

            logger.info("Airport data cached successfully.");
            return airportList;
        }

        // Cache hit
        logger.info("Cache hit: Returning cached airport data.");
        return cachedAirports;
    }

    private void simulateSlowService() {
        logger.info("Simulating slow service call to fetch airport data...");
        try {
            Thread.sleep(3000); // Simulating delay
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
