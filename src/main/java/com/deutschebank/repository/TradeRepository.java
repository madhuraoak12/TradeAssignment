package com.deutschebank.repository;

import com.deutschebank.domain.Book;
import com.deutschebank.domain.Trade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TradeRepository extends CrudRepository<Trade, Integer> {
    public List<Trade> findById(int id);

    @Query("SELECT t FROM Trade t WHERE t.book.id = :bookId AND t.tradeId = :tradeId")
    public List<Trade> findByBookAndTradeId(@Param("bookId") int bookId,
                                            @Param("tradeId") String tradeId);
}
