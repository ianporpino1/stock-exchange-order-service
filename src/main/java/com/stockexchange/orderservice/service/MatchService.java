package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.client.MatchingClient;
import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import com.stockexchange.orderservice.model.dto.MatchResponse;
import org.springframework.stereotype.Service;


@Service
public class MatchService {
    private final MatchingClient matchingClient;


    public MatchService(MatchingClient matchingClient) {
        this.matchingClient = matchingClient;
    }

    public MatchResponse match(CreateOrderCommand command) {
        return matchingClient.match(command);
    }
}
