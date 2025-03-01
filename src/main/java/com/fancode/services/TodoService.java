package com.fancode.services;

import com.fancode.api.RestClient;
import com.fancode.model.Todo;
import io.restassured.response.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TodoService {
    private static final Logger logger = LogManager.getLogger(TodoService.class);
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public TodoService() {
        this.restClient = new RestClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<Todo> getAllTodos() {
        try {
            Response response = restClient.get("/todos");
            if (response.getStatusCode() == 200) {
                return objectMapper.readValue(
                        response.getBody().asString(),
                        new TypeReference<List<Todo>>() {}
                );
            } else {
                logger.error("Failed to get todos. Status code: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while getting todos", e);
        }
        return new ArrayList<>();
    }

    public List<Todo> getTodosByUserId(int userId) {
        try {
            Response response = restClient.get("/todos?userId=" + userId);
            if (response.getStatusCode() == 200) {
                return objectMapper.readValue(
                        response.getBody().asString(),
                        new TypeReference<List<Todo>>() {}
                );
            } else {
                logger.error("Failed to get todos for user ID: {}. Status code: {}", userId, response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while getting todos for user ID: {}", userId, e);
        }
        return new ArrayList<>();
    }

    public double calculateCompletionPercentage(int userId) {
        List<Todo> userTodos = getTodosByUserId(userId);

        if (userTodos.isEmpty()) {
            logger.warn("No todos found for user ID: {}", userId);
            return 0.0;
        }

        int totalTodos = userTodos.size();
        int completedTodos = 0;
        for (Todo todo : userTodos) {
            if (todo.isCompleted()) {
                completedTodos++;
            }
        }

        double completionPercentage = ((double) completedTodos / totalTodos) * 100;

        logger.info("User ID: {}, Total Todos: {}, Completed Todos: {}, Completion Percentage: {}%",
                userId, totalTodos, completedTodos, completionPercentage);

        return completionPercentage;
    }
}