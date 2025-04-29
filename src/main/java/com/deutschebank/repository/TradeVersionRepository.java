package com.deutschebank.repository;

import com.deutschebank.domain.Trade;
import com.deutschebank.domain.TradeVersion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TradeVersionRepository extends CrudRepository<TradeVersion, Integer> {
    List<TradeVersion> findById(int tradeVersionId);

    @Query("SELECT t FROM TradeVersion t WHERE t.trade.id = :tradeId AND t.versionNumber = :versionNumber")
    List<TradeVersion> findByTradeAndVersionNumber(@Param("tradeId") int tradeId,
                                                   @Param("versionNumber") int versionNumber);

}
