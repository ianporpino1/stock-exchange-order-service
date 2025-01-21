package com.stockexchange.orderservice.client;

import com.stockexchange.orderservice.controller.dto.MatchRequest;
import com.stockexchange.orderservice.controller.dto.MatchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient("matching-service")
public interface MatchingClient {
    
    @RequestMapping(method = RequestMethod.POST, value ="/match",consumes ="application/json")
    MatchResponse match(@RequestBody MatchRequest request);
    
}
