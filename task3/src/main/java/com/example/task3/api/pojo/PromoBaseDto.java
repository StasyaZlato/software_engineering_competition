package com.example.task3.api.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.task3.database.entities.PromoBase;

import java.util.Optional;

public class PromoBaseDto {
    public final String name;
    public final Optional<String> description;

    @JsonCreator
    public PromoBaseDto(@JsonProperty(required = true) String name,
                        @JsonProperty Optional<String> description) {
        this.name = name;
        this.description = description;
    }

    public PromoBaseDto(PromoBase promoBase) {
        this.name = promoBase.name;
        this.description = promoBase.description;
    }
}
