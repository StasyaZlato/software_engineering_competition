package com.example.task3.api.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.task3.database.entities.Prize;

public class PrizeDto {
    public final String description;
    public PrizeDto(Prize prize) {
        this.description = prize.description;
    }

    @JsonCreator
    public PrizeDto(@JsonProperty(required = true) String description) {
        this.description = description;
    }
}
