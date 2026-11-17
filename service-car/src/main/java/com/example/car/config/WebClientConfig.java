package com.example.car.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Registers a load balanced WebClient.Builder.
 *
 * <p>@LoadBalanced is what allows urls like http://SERVICE-CLIENT/api/clients/1
 * to be resolved through Eureka. Without it Spring cannot resolve the logical
 * name and fails with "No instances available for SERVICE-CLIENT".</p>
 */
@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
