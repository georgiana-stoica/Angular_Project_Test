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

    @XmlElementWrapper(name = "clase")
    @XmlElement(name = "clasa")
    private List<Clasa> clase;

    @XmlElementWrapper(name = "echipamente")
    @XmlElement(name = "echipament")
    private List<Echipament> echipamente;

}
