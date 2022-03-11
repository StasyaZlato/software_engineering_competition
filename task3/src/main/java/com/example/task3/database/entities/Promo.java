package com.example.task3.database.entities;

import java.util.List;
import java.util.Optional;

public class Promo extends PromoBase {
    public List<Prize> prizes;
    public List<Participant> participants;

    public Promo(long id, String name, Optional<String> description, List<Prize> prizes, List<Participant> participants) {
        super(id, name, description);
        this.prizes = prizes;
        this.participants = participants;
    }

    public Promo(PromoBase promoBase, List<Prize> prizes, List<Participant> participants) {
        super(promoBase.id, promoBase.name, promoBase.description);
        this.prizes = prizes;
        this.participants = participants;
    }

    public Promo() {
    }

    public Promo(long id, String name, Optional<String> description) {
        super(id, name, description);
    }

    public void setPrizes(List<Prize> prizes) {
        this.prizes = prizes;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
