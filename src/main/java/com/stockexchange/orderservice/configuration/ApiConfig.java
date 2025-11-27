package com.stockexchange.orderservice.configuration;


import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class ApiConfig {

    @Bean
    @LoadBalanced
    WebClient.Builder webClientBuilder(ObjectProvider<WebClientCustomizer> customizers) {
        WebClient.Builder builder = WebClient.builder();
        customizers.orderedStream().forEach(c -> c.customize(builder));
        return builder;
    }


    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor() {
        return new VirtualThreadTaskExecutor();
    }
}
