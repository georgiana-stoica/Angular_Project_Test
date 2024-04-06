package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Contact {

    @XmlElement
    private String telefon;

    @XmlElement
    private String email;

    // Getters și Setters
}
