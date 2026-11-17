package com.example.car.web;

import com.example.car.entities.Car;
import com.example.car.repositories.CarRepository;
import com.example.car.services.ClientApi;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Car endpoints, fully reactive.
 *
 * <p>service-car runs on WebFlux (Netty): blocking a handler thread with block()
 * throws "block()/blockFirst()/blockLast() are blocking, which is not supported
 * in thread reactor-http-nio-x". The owner lookup is therefore composed as a
 * Mono and each car is enriched asynchronously.</p>
 */
@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarRepository carRepository;
    private final ClientApi clientApi;

    public CarController(CarRepository carRepository, ClientApi clientApi) {
        this.carRepository = carRepository;
        this.clientApi = clientApi;
    }

    @GetMapping
    public Flux<Car> findAll() {
        return Flux.fromIterable(carRepository.findAll())
                .flatMap(this::enrich);
    }

    @GetMapping("/{id}")
    public Mono<Car> findById(@PathVariable Long id) {
        return Mono.justOrEmpty(carRepository.findById(id))
                .flatMap(this::enrich)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Car not found with ID: " + id)));
    }

    @GetMapping("/client/{clientId}")
    public Flux<Car> findByClient(@PathVariable Long clientId) {
        return Flux.fromIterable(carRepository.findByClientId(clientId));
    }

    @PostMapping
    public Mono<Car> save(@RequestBody Car car) {
        return Mono.just(carRepository.save(car));
    }

    /**
     * A failure of SERVICE-CLIENT must not break the listing: the owner stays
     * null and the car is still returned.
     */
    private Mono<Car> enrich(Car car) {
        return clientApi.monoClientById(car.getClientId())
                .doOnNext(car::setClient)
                .onErrorResume(e -> {
                    System.err.println("Could not fetch client " + car.getClientId() + ": " + e.getMessage());
                    return Mono.empty();
                })
                .thenReturn(car);
    }
}
