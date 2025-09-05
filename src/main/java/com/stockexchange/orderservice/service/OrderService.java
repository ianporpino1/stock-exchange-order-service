package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.client.MatchingClient;
import com.stockexchange.orderservice.model.OrderStatus;
import com.stockexchange.orderservice.model.Trade;
import com.stockexchange.orderservice.model.dto.*;
import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MatchingClient matchingClient;
    private final WalService logService;
    private final TradeService tradeService;

    public OrderService(OrderRepository orderRepository, MatchingClient matchingClient, WalService logService, TradeService tradeService) {
        this.orderRepository = orderRepository;
        this.matchingClient = matchingClient;
        this.logService = logService;

        this.tradeService = tradeService;
    }

//
//
//    public OrderResponse createOrder(OrderRequest orderRequest, UUID userId) {
//        //validar ordem
//        //talvez salvar symbols no cache/db
//        //fazer WAL
//        //qual sera a abordagem que vai mostrar uma diferenca de desempenho maior,
//        //retornar pro cliente sem esperar o matching ou esperar o matching e retornar a ordem
//        UUID commandId = UUID.randomUUID();
//        UUID orderId = UUID.randomUUID();
//
//        CreateOrderCommand command = new CreateOrderCommand(
//                orderRequest,
//                commandId,
//                orderId,
//                userId
//        );
//
//        //WAL - Write Ahead Log - salvar o comando em um log antes de enviar pro matching
//        logService.log(command);
//
//        //structured concurrency p fazer essa chamada de forma assincrona
//        //e retornar a resposta mais rapido pro cliente
//        //porem nesse caso, o cliente nao tera a ordem atualizada com o status executed
//        MatchResponse matchResponse = matchingClient.match(command);
//
//        List<Order> orders = new ArrayList<>();
//
//        OrderResponse responseForClient = null;
//        List<OrderResponse> sortedOrders = matchResponse.orders().stream()
//                .filter(orderResponse -> orderResponse.userId().equals(userId))
//                .sorted(Comparator.comparing(OrderResponse::orderDate).reversed())
//                .toList();
//        for(OrderResponse orderResponse: sortedOrders){
//            Order order = convertOrderResponseToOrder(orderResponse);
//            orders.add(order);
//
//            if (responseForClient == null) {
//                responseForClient = orderResponse;
//            }
//        }
//        orderRepository.saveAll(orders);
//
//        if(!matchResponse.trades().isEmpty()){
//            List<Trade> trades = new ArrayList<>();
//            for(TradeResponse tradeResponse: matchResponse.trades()){
//                Trade trade = new Trade(
//                        tradeResponse.tradeId(),
//                        tradeResponse.buyOrderId(),
//                        tradeResponse.sellOrderId(),
//                        tradeResponse.buyerUserId(),
//                        tradeResponse.sellerUserId(),
//                        tradeResponse.symbol(),
//                        tradeResponse.quantity(),
//                        tradeResponse.price(),
//                        tradeResponse.executedAt()
//                );
//                trades.add(trade);
//            }
//            tradeService.saveAllTrades(trades);
//        }
//
//        return responseForClient;
//    }
//
//    private Order convertOrderResponseToOrder(OrderResponse orderResponse) {
//        return new Order(
//                orderResponse.orderId(),
//                orderResponse.symbol(),
//                orderResponse.orderType(),
//                orderResponse.orderStatus(),
//                orderResponse.price(),
//                orderResponse.executedQuantity(),
//                orderResponse.totalQuantity(),
//                orderResponse.orderDate(),
//                orderResponse.userId()
//
//        );
//    }
//





    public OrderResponse getOrder(UUID orderId, UUID userId) {
        Order order = orderRepository.findOrderByOrderId(orderId);
        if (!order.getUserId().equals(userId)) {
            return null;
        }
        return new OrderResponse(order);
    }

    public OrderResponse findOrderByIdWithSync(UUID orderId, UUID userId) {
        Order localOrder = orderRepository.findOrderByOrderId(orderId);
        if (!localOrder.getUserId().equals(userId)) {
            return null;
        }

        if (localOrder.getStatus().equals(OrderStatus.ACCEPTED) || localOrder.getStatus().equals(OrderStatus.PARTIALLY_EXECUTED)) {
            OrderResponse liveStatus = matchingClient.getOrderById(orderId);
            if (!liveStatus.orderStatus().equals(localOrder.getStatus())) {
                localOrder.setStatus(liveStatus.orderStatus());
                localOrder.setExecutedQuantity(liveStatus.executedQuantity());
                orderRepository.save(localOrder);
            }
        }

        return new OrderResponse(localOrder);
    }

    public List<CreateOrderCommand> recoverOrders() {
        return logService.readAll();
    }

    public void saveAll(List<Order> orders) {
        orderRepository.saveAll(orders);
    }
}
