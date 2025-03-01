package com.fancode.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Todo {
    private int userId;
    private int id;
    private String title;
    private boolean completed;
}