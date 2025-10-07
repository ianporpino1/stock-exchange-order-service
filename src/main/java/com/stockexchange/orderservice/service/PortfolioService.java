package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.client.PortfolioClient;
import com.stockexchange.orderservice.model.dto.MatchResponse;
import com.stockexchange.orderservice.model.dto.TradeListResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PortfolioService {
    private final PortfolioClient portfolioClient;

    public PortfolioService(PortfolioClient portfolioClient) {
        this.portfolioClient = portfolioClient;
    }

    public Mono<Void> handlePortfolioUpdates(MatchResponse matchResponse) {
        if (matchResponse.trades().isEmpty()) {
            return Mono.empty();
        }
        return portfolioClient.processExecutedTrades(
                new TradeListResponse(matchResponse.trades())
        );
    }
}
