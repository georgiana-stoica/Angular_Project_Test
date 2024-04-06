package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.List;

@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Membru {

    @XmlAttribute
    private String id;

    @XmlElement
    private String nume;

//    @XmlElementWrapper(name = "rezervari")
//    @XmlElement(name = "rezervare")
//    private List<Rezervare> rezervari;

}
