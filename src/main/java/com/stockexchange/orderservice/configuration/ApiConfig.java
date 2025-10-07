package com.stockexchange.orderservice.configuration;

import com.stockexchange.orderservice.client.MatchingClient;
import com.stockexchange.orderservice.client.PortfolioClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.client.RestClientCustomizer;
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
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ApiConfig {

    @Bean
    @LoadBalanced
    RestClient.Builder restClientBuilder(ObjectProvider<RestClientCustomizer> customizers) {
        RestClient.Builder builder = RestClient.builder();
        customizers.orderedStream().forEach(c -> c.customize(builder));
        return builder;
    }

    @Bean
    public MatchingClient matchingClient(RestClient.Builder builder) {
        RestClient restClient = builder
                .baseUrl("http://matching-service")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(MatchingClient.class);
    }
    @Bean
    public PortfolioClient portfolioClient(RestClient.Builder builder) {
        RestClient restClient = builder
                .baseUrl("http://portfolio-service")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
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
