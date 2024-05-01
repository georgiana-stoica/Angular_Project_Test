package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SalaDeFitnessWrapper {

    @JsonProperty("sala_de_fitness")
    private SalaDeFitness salaDeFitness;

}
