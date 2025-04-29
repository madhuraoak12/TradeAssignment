package com.deutschebank.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class CounterParty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="COUNTER_PARTY_ID")
    private int id;

    @Column(name = "COUNTER_PARTY_NAME")
    private String counterPartyName;

    public int getId() {
        return id;
    }

    public void setId(int counterPartyId) {
        this.id = counterPartyId;
    }

    public String getCounterPartyName() {
        return counterPartyName;
    }

    public void setCounterPartyName(String counterPartyName) {
        this.counterPartyName = counterPartyName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CounterParty that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CounterParty{" +
                "id=" + id +
                ", counterPartyName='" + counterPartyName + '\'' +
                '}';
    }
}
