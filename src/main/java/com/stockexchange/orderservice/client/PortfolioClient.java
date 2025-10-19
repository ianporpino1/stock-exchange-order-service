package com.stockexchange.orderservice.client;

import com.stockexchange.orderservice.model.dto.TradeListResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

@Service
public class PortfolioClient {

    private final WebClient webClient;

    public PortfolioClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://portfolio-service").build();
    }

    public Mono<Void> processExecutedTrades(TradeListResponse trades) {
        String mutation = """
            mutation ProcessTrades($tradesInput: TradeListInput!) {
              processTrades(trades: $tradesInput)
            }
        """;

        MatchingClient.GraphQLRequest graphQLRequest = new MatchingClient.GraphQLRequest(
                mutation,
                Map.of("tradesInput", trades)
        );

        return webClient.post()
                .uri("/graphql")
                .bodyValue(graphQLRequest)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<MatchingClient.GraphQLResponse<Map<String, Boolean>>>() {})
                .flatMap(response -> {
                    if (response.errors() != null) {
                        System.err.println("Erro retornado pelo portfolio-service: " + response.errors());
                        return Mono.error(new RuntimeException("Falha ao processar trades no portfolio-service."));
                    }
                    return Mono.empty();
                })
                .then();
    }
}
