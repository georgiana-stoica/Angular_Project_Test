package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
//@XmlAccessorType(XmlAccessType.FIELD)
public class SalaDeFitnessWrappper {

    @JsonProperty("sala_de_fitness")
    private SalaDeFitness salaDeFitness;

//    @XmlElement(name = "sala_de_fitness")
//    private SalaDeFitness sala_de_fitness;

}
