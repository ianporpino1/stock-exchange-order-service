package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.Trade;
import com.stockexchange.orderservice.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {
    
    @Autowired
    private TradeRepository tradeRepository;
    
    public void saveAllTrades(List<Trade> trades) {
        tradeRepository.saveAll(trades);
    }
}
