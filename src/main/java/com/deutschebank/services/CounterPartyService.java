package com.deutschebank.services;

import com.deutschebank.VO.TradeVO;
import com.deutschebank.domain.CounterParty;
import com.deutschebank.repository.CounterPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CounterPartyService {
    @Autowired
    private CounterPartyRepository counterPartyRepository;

    public CounterParty findOrSaveCounterParty(TradeVO tradeVO) {
        List<CounterParty> counterParties    = (List<CounterParty>) counterPartyRepository.
                findByCounterPartyName(tradeVO.getCounterPartyId());
        CounterParty counterParty = null;
        if(counterParties != null && !counterParties.isEmpty()) {
            counterParty = counterParties.get(0);
        }
        else {
            counterParty = new CounterParty();
            counterParty.setCounterPartyName(tradeVO.getCounterPartyId());
            counterPartyRepository.save(counterParty);
        }
        return counterParty;
    }
}
