package com.fancode.services;

import com.fancode.api.RestClient;
import com.fancode.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public UserService() {
        this.restClient = new RestClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Get all users from the API
     */
    public List<User> getAllUsers() {
        try {
            Response response = restClient.get("/users");
            if (response.getStatusCode() == 200) {
                return objectMapper.readValue(
                        response.getBody().asString(),
                        new TypeReference<List<User>>() {}
                );
            } else {
                logger.error("Failed to get users. Status code: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while getting users", e);
        }
        return new ArrayList<>();
    }

    /**
     * Check if a user belongs to FanCode city based on geo coordinates
     */
    public boolean isUserFromFanCodeCity(User user, String cityName, double minLat, double maxLat, double minLong, double maxLong) {
        if (user == null || user.getAddress() == null || user.getAddress().getGeo() == null) {
            return false;
        }

        double lat = user.getAddress().getGeo().getLatitude();
        double lng = user.getAddress().getGeo().getLongitude();

        boolean isInRange = (lat >= minLat && lat <= maxLat) && (lng >= minLong && lng <= maxLong);

        logger.info("User {} from city {} coordinates: lat={}, lng={}, isFromFanCodeCity={}",
                user.getId(), cityName, lat, lng, isInRange);

        return isInRange;
    }

    /**
     * Get all users from FanCode city
     */
    public List<User> getUsersFromFanCodeCity(String cityName, double minLat, double maxLat, double minLong, double maxLong) {
        List<User> allUsers = getAllUsers();
        List<User> fanCodeUsers = new ArrayList<>();

        for (User user : allUsers) {
            if (isUserFromFanCodeCity(user, cityName, minLat , maxLat,minLong, maxLong )) {
                fanCodeUsers.add(user);
                logger.info("User {} belongs to {} city", user.getId(), cityName);
            }
        }

        logger.info("Found {} users from {} city", fanCodeUsers.size(), cityName);
        return fanCodeUsers;
    }
}