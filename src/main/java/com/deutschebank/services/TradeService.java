package com.deutschebank.services;

import com.deutschebank.VO.TradeVO;
import com.deutschebank.domain.Book;
import com.deutschebank.domain.CounterParty;
import com.deutschebank.domain.Trade;
import com.deutschebank.domain.TradeVersion;
import com.deutschebank.exception.InvalidTradeException;
import com.deutschebank.repository.BookRepository;
import com.deutschebank.repository.CounterPartyRepository;
import com.deutschebank.repository.TradeRepository;
import com.deutschebank.repository.TradeVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TradeService {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private TradeVersionRepository tradeVersionRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private CounterPartyService counterPartyService;

    private TradeVersion createTradeVersion(Trade trade, TradeVO tradeVO) {
        CounterParty counterParty = counterPartyService.findOrSaveCounterParty(tradeVO);
        var tradeVersion = new TradeVersion();
        tradeVersion.setTrade(trade);
        tradeVersion.setVersionNumber(tradeVO.getVersion());
        tradeVersion.setCounterParty(counterParty);
        tradeVersion.setMaturityDate(LocalDate.parse(tradeVO.getMaturityDate(), formatter));
        tradeVersion.setCreatedDate(LocalDate.parse(tradeVO.getCreatedDate(), formatter));
        if(tradeVersion.getMaturityDate().isBefore(tradeVersion.getCreatedDate())) {
            tradeVersion.setExpired('Y');
        }
        else {
            tradeVersion.setExpired('N');
        }
        tradeVersionRepository.save(tradeVersion);
        return tradeVersion;
    }

    private void validateTradeVersions(List<TradeVersion> tradeVersions,
                                                           TradeVO tradeVO) throws InvalidTradeException {
        TreeMap<Integer,TradeVersion> tradeVersionMap = tradeVersions.stream()
                .collect(Collectors.toMap(TradeVersion::getVersionNumber, Function.identity(),
                        (k1,k2) -> k1,
                        TreeMap::new));
        if(tradeVO.getVersion() < tradeVersionMap.lastKey()) {
            throw new InvalidTradeException("Higher version of trade exists");
        }
    }

    public TradeVersion saveOrUpdateTradeVersion(Trade trade, TradeVO tradeVO) throws InvalidTradeException{
        TradeVersion tradeVersion = null;
        List<TradeVersion> tradeVersions = tradeVersionRepository.findByTradeAndVersionNumber(
                trade.getId(),tradeVO.getVersion());
        if(!tradeVersions.isEmpty()) {
            validateTradeVersions(tradeVersions, tradeVO);
            tradeVersion = tradeVersions.get(0);
        }
        else {
            tradeVersion = createTradeVersion(trade, tradeVO);
        }
        return tradeVersion;
    }

    @Transactional
    public TradeVersion saveOrUpdateTradeVersion(TradeVO tradeVO) throws InvalidTradeException{
        TradeVersion tradeVersion = null;
        Trade trade = saveOrUpdateTrade(tradeVO);
        trade.getTradeVersions();
        if(!trade.getTradeVersions().isEmpty()) {
            tradeVersion = saveOrUpdateTradeVersion(trade, tradeVO);
        }
        else {
            tradeVersion = createTradeVersion(trade, tradeVO);
        }
        return tradeVersion;
    }

    public Trade saveOrUpdateTrade(TradeVO tradeVO) {
        Book book = bookService.findOrCreateBook(tradeVO);
        Trade trade = null;
        List<Trade> trades = (List<Trade>) tradeRepository.findByBookAndTradeId(book.getId(), tradeVO.getTradeId());
        if(trades != null && !trades.isEmpty()) {
            trade = trades.get(0);
        }
        else {
            trade = saveTrade(book, tradeVO);
        }
        return trade;
    }

    private Trade saveTrade(Book book, TradeVO tradeVO) {
        Trade trade = new Trade();
        trade.setTradeId(tradeVO.getTradeId());
        trade.setBook(book);
        tradeRepository.save(trade);
        return trade;
    }
}
