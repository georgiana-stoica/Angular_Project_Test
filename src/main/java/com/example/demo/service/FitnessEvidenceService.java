package com.example.demo.service;

import com.example.demo.model.Activitate;
import com.example.demo.model.Activitati;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
public class FitnessEvidenceService {

  @Value("${fitness.xml.path}")
  private String xmlFilePath;

  public List<Activitate> parseActivitatiFromXML() {
    try {
      File file = new File(xmlFilePath);
      JAXBContext jaxbContext = JAXBContext.newInstance(Activitati.class); // Schimbă la Activitati.class
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      Activitati activitati = (Activitati) jaxbUnmarshaller.unmarshal(file); // Cast la Activitati
      return activitati.getActivitate(); // Returnează lista de activități
    } catch (JAXBException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }
}

