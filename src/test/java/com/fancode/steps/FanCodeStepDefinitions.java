package com.fancode.steps;

import com.fancode.model.Todo;
import com.fancode.model.User;
import com.fancode.services.TodoService;
import com.fancode.services.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

public class FanCodeStepDefinitions {
    private static final Logger logger = LogManager.getLogger(FanCodeStepDefinitions.class);

    private final UserService userService;
    private final TodoService todoService;
    private List<User> fanCodeUsers;

    public FanCodeStepDefinitions() {
        this.userService = new UserService();
        this.todoService = new TodoService();
    }

    @Given("User has the todo tasks")
    @Description("Get all todo tasks")
    public void get_todo_tasks() {
        logger.info("Getting all todos task");
        List<Todo> todos = todoService.getAllTodos();
        Assert.assertFalse(todos.isEmpty(), "TODO API should return tasks");
        logger.info("Successfully accessed TODO API, found {} users", todos.size());
    }

    @And("User belongs to the city {string} with lat between {double} and {double} and long between {double} and {double}")
    @Description("Get all users from FanCode city")
    public void all_users_from_fan_code_city(String cityName, double minLat, double maxLat, double minLong, double maxLong) {
        logger.info("Identifying users from FanCode city");
        fanCodeUsers = userService.getUsersFromFanCodeCity(cityName, minLat,maxLat,minLong, maxLong );
        Assert.assertFalse(fanCodeUsers.isEmpty(), String.format("There are no users from the {} city", cityName));
        logger.info("Found {} users from {} city", fanCodeUsers.size(), cityName);
    }

    @Then("User Completed task percentage should be greater than {int}%")
    @Description("Get user completed task percentage")
    @Severity(SeverityLevel.CRITICAL)
    public void all_fan_code_city_users_should_have_more_than_percent_of_their_todos_completed(Integer threshold) {
        logger.info("Calculating todo completion percentages");
        List<String> failedUsers = new ArrayList<>();
        Map<Integer, Double> completionPercentages = new HashMap<>();

        // Calculate percentages and identify failures
        for (User user : fanCodeUsers) {
            int userId = user.getId();
            double completionPercentage = todoService.calculateCompletionPercentage(userId);
            completionPercentages.put(userId, completionPercentage);
            logger.info("User ID: {}, Completion Percentage: {}%", userId, completionPercentage);

            // Track users below threshold
            if (completionPercentage <= threshold) {
                failedUsers.add(String.format("User ID: %d has only %.1f%% completion rate",
                        userId, completionPercentage));
            }
        }

        // Fail with all errors if any users didn't meet the threshold
        if (!failedUsers.isEmpty()) {
            Assert.fail("Found " + failedUsers.size() + " users below the " + threshold +
                    "% threshold:\n" + failedUsers);
        }
    }
}