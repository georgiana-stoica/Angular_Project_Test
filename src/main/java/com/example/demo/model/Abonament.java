package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Abonament {

    @XmlAttribute
    private String tip;

    @XmlAttribute(name = "data_inceput")
    private String data_inceput;

    @XmlAttribute(name = "data_sfarsit")
    private String data_sfarsit;

}
