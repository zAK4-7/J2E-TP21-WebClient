package com.example.car;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SERVICE-CAR microservice (port 8082).
 *
 * <p>The owner is resolved with a @LoadBalanced WebClient (see WebClientConfig),
 * so the logical name SERVICE-CLIENT is resolved through Eureka.</p>
 */
@EnableDiscoveryClient
@SpringBootApplication
public class CarApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarApplication.class, args);
    }
}
