package com.example.task3.database.entities;

public class Participant {
    public long id;
    public String name;

    public Participant() {
    }

    public Participant(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
