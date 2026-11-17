package com.example.car.web;

import com.example.car.entities.Client;
import com.example.car.services.ClientApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Diagnostic endpoint proving that WebClient + @LoadBalanced reaches
 * SERVICE-CLIENT by its logical name.
 *
 * <p>The return type is Mono: a handler must never block a Netty event loop
 * thread, so block() is only used outside a reactive thread.</p>
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final ClientApi clientApi;

    public TestController(ClientApi clientApi) {
        this.clientApi = clientApi;
    }

    @GetMapping("/client/{id}")
    public Mono<Client> testClient(@PathVariable Long id) {
        return clientApi.monoClientById(id);
    }
}
