package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Rezervare {

    @XmlAttribute(name = "clasa_id")
    private String clasa_id;

    @XmlAttribute(name = "data")
    private String data;

}
