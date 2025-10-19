package com.stockexchange.orderservice.client;

import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import com.stockexchange.orderservice.model.dto.MatchResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

@Service
public class MatchingClient {

    private final WebClient webClient;

    public MatchingClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://matching-service").build();
    }

    public Mono<MatchResponse> match(CreateOrderCommand request) {
        String mutation = """
            mutation MatchOrder($order: CreateOrderCommandInput!) {
              matchOrder(order: $order) {
                orders {
                  orderId
                  orderStatus
                  totalQuantity
                  executedQuantity
                }
                trades {
                  tradeId
                  symbol
                  quantity
                  price
                  executedAt
                  buyOrderId
                  sellOrderId
                  buyerUserId
                  sellerUserId
                }
              }
            }
        """;

        GraphQLRequest graphQLRequest = new GraphQLRequest(
                mutation,
                Map.of("order", request)
        );

        return webClient.post()
                .uri("/graphql")
                .bodyValue(graphQLRequest)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<GraphQLResponse<MatchData>>() {})
                .flatMap(response -> {
                    if (response.errors() != null) {
                        System.err.println("Erro retornado pelo matching-service: " + response.errors());
                        return Mono.error(new RuntimeException("Falha na mutação 'matchOrder'."));
                    }
                    if (response.data() == null) {
                        return Mono.error(new RuntimeException("Resposta do matching-service não continha dados."));
                    }
                    return Mono.just(response.data().matchOrder());
                });
    }

    public record GraphQLRequest(String query, Map<String, Object> variables) { }
    public record GraphQLResponse<T>(T data, Object errors) { }

    public record MatchData(MatchResponse matchOrder) {}}