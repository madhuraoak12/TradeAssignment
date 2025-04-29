package com.deutschebank.repository;

import com.deutschebank.domain.Book;
import com.deutschebank.domain.Trade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DataJpaTest
public class TradeRepositoryTest {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private BookRepository bookRepository;

    private Book book;
    private Trade trade;

    private void insertTrade(String bookId, String tradeId) {
        book = new Book();
        book.setBookName(bookId);
        bookRepository.save(book);

        trade = new Trade();
        trade.setTradeId(tradeId);
        trade.setBook(book);
        tradeRepository.save(trade);
    }

    @BeforeEach
    public void setUp(){
        insertTrade("B1", "T1");
    }

    @AfterEach
    public void tearDown(){
        tradeRepository.delete(trade);
    }

    @Test
    public void testFindByBookAndTradeId() {
        List<Trade> trades = tradeRepository.findByBookAndTradeId(book.getId(), trade.getTradeId());
        assertNotNull(trades);
        assertEquals(1,trades.size());
        assertNotNull(trades.get(0));
        assertEquals(trade.getId(),trades.get(0).getId());
    }
}
