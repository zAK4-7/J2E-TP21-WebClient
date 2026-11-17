package com.example.car;

import com.example.car.entities.Car;
import com.example.car.repositories.CarRepository;
import com.example.car.services.ClientApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * service-car is a WebFlux application (needed for WebClient), so the HTTP
 * layer is tested with WebTestClient instead of MockMvc.
 *
 * <p>No Eureka registry runs during the tests: the owner lookup is expected to
 * fail and the car must still be returned without its client.</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CarControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ClientApi clientApi;

    @BeforeEach
    void clean() {
        carRepository.deleteAll();
    }

    @Test
    void carsAreListedEvenWithoutTheClientService() {
        carRepository.save(new Car(null, "Toyota", "Corolla", 1L, null));

        webTestClient.get().uri("/api/cars")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].marque").isEqualTo("Toyota")
                .jsonPath("$[0].client").doesNotExist();
    }

    @Test
    void unknownCarReturns404() {
        webTestClient.get().uri("/api/cars/9999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void findByClientIdFiltersLocally() {
        carRepository.save(new Car(null, "Toyota", "Corolla", 1L, null));
        carRepository.save(new Car(null, "Peugeot", "301", 2L, null));

        webTestClient.get().uri("/api/cars/client/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1);
    }

    @Test
    void webClientConfigBeanIsPresent() {
        assertNotNull(clientApi, "ClientApi must receive the @LoadBalanced WebClient.Builder");
    }

    @Test
    void callingAClientWithoutDiscoveryFailsExplicitly() {
        // Without a Eureka registry the load balancer cannot resolve SERVICE-CLIENT
        assertThrows(Exception.class, () -> clientApi.findClientById(1L));
    }
}
