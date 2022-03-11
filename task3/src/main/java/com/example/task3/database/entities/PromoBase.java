package com.example.task3.database.entities;

import java.util.Optional;

public class PromoBase {
    public long id;
    public String name;
    public Optional<String> description;

    public PromoBase() {
    }

    public PromoBase(long id, String name, Optional<String> description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(Optional<String> description) {
        this.description = description;
    }
}
