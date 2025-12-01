package com.stockexchange.orderservice.handler;

import com.stockexchange.orderservice.model.Trade;
import com.stockexchange.orderservice.model.dto.TradeResponse;
import com.stockexchange.orderservice.model.event.TradeExecutedEvent;
import com.stockexchange.orderservice.service.TickerService;
import com.stockexchange.orderservice.service.TradeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class TradeHandler {
    private  final TradeService tradeService;
    private final TickerService tickerService;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TradeHandler.class);

    public TradeHandler(TradeService tradeService, TickerService tickerService) {
        this.tradeService = tradeService;
        this.tickerService = tickerService;
    }

    @Bean
    public Function<Flux<TradeExecutedEvent>, Mono<Void>> handleTrade(){
        return flux -> flux
                .filter(Objects::nonNull)
                .concatMap(event -> {

                    Mono<Void> saveOp = tradeService.handleTrade(new TradeResponse(
                            event.tradeId(),
                            event.buyOrderId(),
                            event.sellOrderId(),
                            event.buyerUserId(),
                            event.sellerUserId(),
                            event.symbol(),
                            event.quantity(),
                            event.price(),
                            event.executedAt()));

                    Mono<Void> tickerOp = tickerService.updateLastPrice(
                            event.symbol(),
                            event.price(),
                            event.executedAt()
                    );
                    return Mono.when(saveOp, tickerOp.onErrorResume(e -> {
                        log.error("Erro ao atualizar ticker: " + event.symbol(), e);
                        return Mono.empty();
                    }));
                })
                .then();
    }

}
