package com.example.task3.api.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.task3.database.entities.Promo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PromoDto extends PromoBaseDto {
    public final List<PrizeDto> prizes;
    public final List<ParticipantDto> participants;

    @JsonCreator
    public PromoDto(
            @JsonProperty(required = true) String name,
            @JsonProperty Optional<String> description,
            @JsonProperty List<PrizeDto> prizes,
            @JsonProperty List<ParticipantDto> participants) {
        super(name, description);
        this.prizes = prizes;
        this.participants = participants;
    }

    public PromoDto(Promo promo) {
        super(promo.name, promo.description);
        this.participants = promo.participants.stream().map(ParticipantDto::new).collect(Collectors.toList());
        this.prizes = promo.prizes.stream().map(PrizeDto::new).collect(Collectors.toList());
    }
}
