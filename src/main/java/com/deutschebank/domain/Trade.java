package com.deutschebank.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TRADE_ID")
    private int id;

    @Column(name="TRADE_NAME")
    private String tradeId;

    @OneToMany(mappedBy = "trade")
    private Set<TradeVersion> tradeVersions = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getId() {
        return id;
    }

    public void setId(int tradeId) {
        this.id = id;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public Set<TradeVersion> getTradeVersions() {
        return tradeVersions;
    }

    public void setTradeVersions(Set<TradeVersion> tradeVersions) {
        this.tradeVersions = tradeVersions;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Trade trade)) return false;
        return id == trade.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", tradeId='" + tradeId  +
                '}';
    }
}
