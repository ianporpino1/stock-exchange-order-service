package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.client.PortfolioClient;
import com.stockexchange.orderservice.model.dto.MatchResponse;
import com.stockexchange.orderservice.model.dto.TradeListResponse;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {
    private final PortfolioClient portfolioClient;

    public PortfolioService(PortfolioClient portfolioClient) {
        this.portfolioClient = portfolioClient;
    }

    public void handlePortfolioUpdates(MatchResponse matchResponse) {
        if(!matchResponse.trades().isEmpty()) {
            portfolioClient.processExecutedTrades(new TradeListResponse(matchResponse.trades()));
        }
    }
}
