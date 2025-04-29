package com.deutschebank.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class TradeVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TRADE_VERSION_ID")
    private int id;

    @Column(name="VERSION")
    private int versionNumber;

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    @ManyToOne
    @JoinColumn(name = "TRADE_ID")
    private Trade trade;

    private LocalDate maturityDate;
    private LocalDate createdDate;
    private char expired;

    @ManyToOne
    @JoinColumn(name = "COUNTER_PARTY_ID")
    private CounterParty counterParty;

    public int getId() {
        return id;
    }

    public void setTradeVersionId(int tradeVersionId) {
        this.id = tradeVersionId;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public char getExpired() {
        return expired;
    }

    public void setExpired(char expired) {
        this.expired = expired;
    }

    public CounterParty getCounterParty() {
        return counterParty;
    }

    public void setCounterParty(CounterParty counterParty) {
        this.counterParty = counterParty;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TradeVersion that)) return false;
        return id == that.id && Objects.equals(trade, that.trade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trade);
    }

    @Override
    public String toString() {
        return "TradeVersion{" +
                "id=" + id + "version=" + versionNumber +
                ", trade=" + trade +
                ", maturityDate=" + maturityDate +
                ", createdDate=" + createdDate +
                ", expired=" + expired +
                ", counterParty=" + counterParty +
                '}';
    }
}
