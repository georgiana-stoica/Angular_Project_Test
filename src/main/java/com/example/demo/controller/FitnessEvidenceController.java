package com.example.demo.controller;

import com.example.demo.model.Membru;
import com.example.demo.model.SalaDeFitness;
import com.example.demo.model.SalaDeFitnessWrappper;
import com.example.demo.service.FitnessEvidenceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
@RequestMapping("/api/fitness")
@CrossOrigin(origins = "http://localhost:4200")
public class FitnessEvidenceController {

    @Autowired
    private FitnessEvidenceService fitnessService;

    @PostMapping("/uploadFile")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            String contentType = file.getContentType();
            Object parsedData;

            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            if ("application/xml".equals(contentType) || Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xml")) {
                parsedData = fitnessService.parseXml(content);
            } else if ("application/json".equals(contentType) || file.getOriginalFilename().endsWith(".json")) {
                parsedData = fitnessService.parseJson(content);
            } else {
                return ResponseEntity.badRequest().body("Unsupported file type");
            }

            // Salvează datele parsate în sesiune
            request.getSession().setAttribute("parsedData", parsedData);

            // For demonstration, just returning the parsed data or a simple success message
            return ResponseEntity.ok(parsedData); // Or, return a confirmation message

        } catch (IOException | JAXBException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }


    @GetMapping("/display")
    public ResponseEntity<?> displayParsedData(HttpServletRequest request) {
        // Retrieve parsed data from session
        Object parsedData = request.getSession().getAttribute("parsedData");

        if (parsedData == null) {
            return ResponseEntity.ok("No data to display. Please upload a file first.");
        }
        return ResponseEntity.ok(parsedData);
    }


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
            String jsonContent = fitnessService.readJsonContent();
            return fitnessService.parseJson(jsonContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/membri/{id}")
    public ResponseEntity<?> getMembruById(@PathVariable String id, HttpServletRequest request) {
        Membru membru = fitnessService.findMembruById(id, request);
        if (membru != null) {
            return ResponseEntity.ok(membru);
        } else {
            return new ResponseEntity<>("Membru nu a fost găsit.", HttpStatus.NOT_FOUND);
        }
    }
}
