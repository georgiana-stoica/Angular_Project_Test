package com.example.demo.controller;

import com.example.demo.model.SalaDeFitness;
import com.example.demo.model.SalaDeFitnessWrappper;
import com.example.demo.service.FitnessEvidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@RestController
@RequestMapping("/api/fitness")
public class FitnessEvidenceController {

    @Autowired
    private FitnessEvidenceService fitnessService;

    @GetMapping("/membri")
    public SalaDeFitness getAllMembri() {
        try {
            String xmlContent = fitnessService.readXmlContent();
            return fitnessService.parseXml(xmlContent);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @GetMapping("/evidenta")
    public SalaDeFitnessWrappper getEvidenta() {
        try {
            return fitnessService.parseJson();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
