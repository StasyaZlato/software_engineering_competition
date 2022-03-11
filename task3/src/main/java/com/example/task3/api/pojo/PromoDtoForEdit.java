package com.example.task3.api.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class PromoDtoForEdit {
    public final Optional<String> name;
    public final Optional<String> description;

    @JsonCreator
    public PromoDtoForEdit(@JsonProperty Optional<String> name,
                        @JsonProperty Optional<String> description) {
        this.name = name;
        this.description = description;
    }
}
