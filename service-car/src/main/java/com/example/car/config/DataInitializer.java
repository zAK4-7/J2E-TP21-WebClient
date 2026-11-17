package com.example.car.config;

import com.example.car.entities.Car;
import com.example.car.repositories.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Seeds cars pointing to the client ids 1 and 2 of SERVICE-CLIENT.
 */
@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    CommandLineRunner initCars(CarRepository carRepository) {
        return args -> {
            if (carRepository.count() > 0) {
                return;
            }
            carRepository.save(new Car(null, "Toyota", "Corolla", 1L, null));
            carRepository.save(new Car(null, "Renault", "Megane", 1L, null));
            carRepository.save(new Car(null, "Peugeot", "301", 2L, null));
            System.out.println("Seeded " + carRepository.count() + " cars.");
        };
    }
}
