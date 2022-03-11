package com.example.task3.api.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.task3.database.entities.Participant;

public class ParticipantDto {
    public final String name;

    public ParticipantDto(Participant participant) {
        this.name = participant.name;
    }

    @JsonCreator
    public ParticipantDto(@JsonProperty(required = true) String name) {
        this.name = name;
    }
}
