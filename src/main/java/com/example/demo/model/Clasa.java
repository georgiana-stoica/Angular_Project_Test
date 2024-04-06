package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Clasa {

    @XmlAttribute
    private String id;

    @XmlElement
    private String titlu;

    @XmlElement
    private String instructor;

    @XmlElement
    private Programare programare;

    @XmlElement
    private String locatie;

    @XmlElement
    private String capacitate_maxima;


}
