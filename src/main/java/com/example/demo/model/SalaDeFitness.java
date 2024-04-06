package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.List;

@NoArgsConstructor
@Data
@XmlRootElement(name = "sala_de_fitness")
@XmlAccessorType(XmlAccessType.FIELD)
public class SalaDeFitness {

    @XmlElementWrapper(name = "membri")
    @XmlElement(name = "membru")
    private List<Membru> membri;

}
