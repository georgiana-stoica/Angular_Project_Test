package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Echipament {

    @XmlAttribute
    private String id;

    @XmlAttribute
    private String tip;

    @XmlElement
    private String denumire;

    @XmlElement
    private String stare;

}
