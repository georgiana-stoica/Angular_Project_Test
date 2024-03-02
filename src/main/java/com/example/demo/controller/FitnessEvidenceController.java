package com.example.demo.controller;

import com.example.demo.model.Activitate;
import com.example.demo.service.FitnessEvidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness")
public class FitnessEvidenceController {

  @Autowired
  private FitnessEvidenceService fitnessService;

  @GetMapping("/activitati")
  public ResponseEntity<List<Activitate>> parseActivitatiFromXML() {
    List<Activitate> activitati = fitnessService.parseActivitatiFromXML(); // Utilizează noua metodă
    if (activitati.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(activitati, HttpStatus.OK);
  }
}
