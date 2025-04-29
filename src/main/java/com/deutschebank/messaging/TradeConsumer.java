package com.deutschebank.messaging;

import com.deutschebank.VO.TradeVO;
import com.deutschebank.domain.Trade;
import com.deutschebank.domain.TradeVersion;
import com.deutschebank.exception.InvalidTradeException;
import com.deutschebank.services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Component
public class TradeConsumer {

    private TradeVersion tradeVersion;

    public TradeVersion getTradeVersion() {
        return tradeVersion;
    }


    @Autowired
    private TradeService tradeService;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public TradeVersion getTrade(TradeVO tradeVO) throws InvalidTradeException {
        tradeVersion = tradeService.saveOrUpdateTradeVersion(tradeVO);
        return tradeVersion;
    }
}
