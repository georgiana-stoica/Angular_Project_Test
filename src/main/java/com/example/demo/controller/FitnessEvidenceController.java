package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.FitnessEvidenceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/fitness")
//@CrossOrigin(origins = "http://localhost:4200")
public class FitnessEvidenceController {

    @Autowired
    private FitnessEvidenceService fitnessService;

@GetMapping("/upload")
public String loadUploadFormAndDisplayData(Model model, HttpServletRequest request) {
    Object parsedData = request.getSession().getAttribute("parsedData");
    if (parsedData != null) {
        model.addAttribute("parsedData", parsedData);

        // Determine if the data is XML (SalaDeFitness) or JSON (SalaDeFitnessWrapper)
        if (parsedData instanceof SalaDeFitness) {
            SalaDeFitness salaDeFitness = (SalaDeFitness) parsedData;
            Map<String, Clasa> claseDetails = new HashMap<>();
            for (Membru membru : salaDeFitness.getMembri()) {
                for (Rezervare rezervare : membru.getRezervari()) {
                    try {
                        Clasa clasa = fitnessService.getClasaById(rezervare.getClasa_id());
                        claseDetails.put(rezervare.getClasa_id(), clasa);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            model.addAttribute("claseDetails", claseDetails);
            model.addAttribute("isWrapper", false);
        } else if (parsedData instanceof SalaDeFitnessWrappper) {
            SalaDeFitnessWrappper wrapper = (SalaDeFitnessWrappper) parsedData;
            Map<String, Clasa> claseDetails = new HashMap<>();
            for (Membru membru : wrapper.getSalaDeFitness().getMembri()) {
                for (Rezervare rezervare : membru.getRezervari()) {
                    try {
                        Clasa clasa = fitnessService.getClasaById(rezervare.getClasa_id());
                        claseDetails.put(rezervare.getClasa_id(), clasa);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            model.addAttribute("claseDetails", claseDetails);
            model.addAttribute("isWrapper", true);
        }

        // Clear the session attribute after adding to model to prevent it from reappearing on refresh
        request.getSession().removeAttribute("parsedData");
    }
    return "upload";
}



    @PostMapping("/uploadFile")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes,
                                   HttpServletRequest request) {
        try {
            String contentType = file.getContentType();
            Object parsedData;

            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            if ("application/xml".equals(contentType) || file.getOriginalFilename().endsWith(".xml")) {
                parsedData = fitnessService.parseXml(content);
            } else if ("application/json".equals(contentType) || file.getOriginalFilename().endsWith(".json")) {
                parsedData = fitnessService.parseJson(content);
            } else {
                redirectAttributes.addFlashAttribute("message", "Unsupported file type");
                return "redirect:/api/fitness/upload";
            }

            request.getSession().setAttribute("parsedData", parsedData);
            redirectAttributes.addFlashAttribute("message", "File successfully uploaded and parsed");
            return "redirect:/api/fitness/upload";

        } catch (IOException | JAXBException e) {
            redirectAttributes.addFlashAttribute("message", "File upload failed: " + e.getMessage());
            return "redirect:/api/fitness/upload";
        }
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
