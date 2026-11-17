package com.example.car.services;

import com.example.car.entities.Client;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * WebClient based client for SERVICE-CLIENT.
 *
 * <p>The url uses the logical service name, resolved by the load balanced
 * builder. Mapped to Mono so callers can decide to be reactive or blocking.</p>
 */
@Service
public class ClientApi {

    private final WebClient.Builder builder;

    public ClientApi(WebClient.Builder builder) {
        this.builder = builder;
    }

    /** Blocking variant, kept simple for the lab. */
    public Client findClientById(Long id) {
        return monoClientById(id).block();
    }

    /** Reactive variant, composable with other Mono/Flux. */
    public Mono<Client> monoClientById(Long id) {
        return builder.build()
                .get()
                .uri("http://SERVICE-CLIENT/api/clients/" + id)
                .retrieve()
                .bodyToMono(Client.class);
    }
}
