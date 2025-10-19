package com.stockexchange.orderservice.configuration;

import com.stockexchange.orderservice.client.MatchingClient;
import com.stockexchange.orderservice.client.PortfolioClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ApiConfig {

    @Bean
    @LoadBalanced
    WebClient.Builder webClientBuilder(ObjectProvider<WebClientCustomizer> customizers) {
        WebClient.Builder builder = WebClient.builder();
        customizers.orderedStream().forEach(c -> c.customize(builder));
        return builder;
    }

    @Bean
    public MatchingClient matchingClient(WebClient.Builder builder) {
        WebClient webClient = builder
                .baseUrl("http://matching-service")
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(MatchingClient.class);
    }
    @Bean
    public PortfolioClient portfolioClient(WebClient.Builder builder) {
        WebClient webClien = builder
                .baseUrl("http://portfolio-service")
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClien);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(PortfolioClient.class);
    }

    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor() {
        return new VirtualThreadTaskExecutor();
    }
}
