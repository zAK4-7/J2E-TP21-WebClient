package com.example.car.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO mirroring the JSON returned by SERVICE-CLIENT.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    private Long id;

    private String nom;

    private Float age;
}
