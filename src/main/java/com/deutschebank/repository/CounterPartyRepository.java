package com.deutschebank.repository;

import com.deutschebank.domain.CounterParty;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CounterPartyRepository extends CrudRepository<CounterParty,Integer> {
    public List<CounterParty> findById(int id);
    public List<CounterParty> findByCounterPartyName(String counterPartyName);
}
