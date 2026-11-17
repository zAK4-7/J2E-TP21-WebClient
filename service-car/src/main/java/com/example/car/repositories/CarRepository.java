package com.example.car.repositories;

import com.example.car.entities.Car;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByClientId(Long clientId);
}
