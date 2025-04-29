package com.deutschebank.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BOOK_ID")
    private int id;

    @Column(name="BOOK_NAME")
    private String bookName;

    @OneToMany(mappedBy = "book")
    public Set<Trade> trades = new HashSet<>();

    public int getId() {
        return id;
    }

    public Set<Trade> getTrades() {
        return trades;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookId(int bookId) {
        this.id = bookId;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Book that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookName='" + bookName;
    }
}
