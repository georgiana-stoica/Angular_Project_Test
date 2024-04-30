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
import java.util.List;
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
                Map<String, String> classesMap = new HashMap<>();
                Map<String, String> instructorsMap = new HashMap<>();
                for (Membru membru : salaDeFitness.getMembri()) {
                    StringBuilder classesBuilder = new StringBuilder();
                    StringBuilder instructorsBuilder = new StringBuilder();
                    for (Rezervare rezervare : membru.getRezervari()) {
                        try {
                            Clasa clasa = fitnessService.getClasaById(rezervare.getClasa_id());
                            if (classesBuilder.length() > 0) {
                                classesBuilder.append(", ");
                                instructorsBuilder.append(", ");
                            }
                            classesBuilder.append(clasa.getTitlu());
                            instructorsBuilder.append(clasa.getInstructor());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    classesMap.put(membru.getId(), classesBuilder.toString());
                    instructorsMap.put(membru.getId(), instructorsBuilder.toString());
                }
                model.addAttribute("classesMap", classesMap);
                model.addAttribute("instructorsMap", instructorsMap);
                model.addAttribute("isNotWrapper", true);
            } else if (parsedData instanceof SalaDeFitnessWrappper) {
                SalaDeFitnessWrappper wrapper = (SalaDeFitnessWrappper) parsedData;
                Map<String, String> classesMap = new HashMap<>();
                Map<String, String> instructorsMap = new HashMap<>();
                for (Membru membru : wrapper.getSalaDeFitness().getMembri()) {
                    StringBuilder classesBuilder = new StringBuilder();
                    StringBuilder instructorsBuilder = new StringBuilder();
                    for (Rezervare rezervare : membru.getRezervari()) {
                        try {
                            Clasa clasa = fitnessService.getClasaById(rezervare.getClasa_id());
                            if (classesBuilder.length() > 0) {
                                classesBuilder.append(", ");
                                instructorsBuilder.append(", ");
                            }
                            classesBuilder.append(clasa.getTitlu());
                            instructorsBuilder.append(clasa.getInstructor());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    classesMap.put(membru.getId(), classesBuilder.toString());
                    instructorsMap.put(membru.getId(), instructorsBuilder.toString());
                }
                model.addAttribute("classesMap", classesMap);
                model.addAttribute("instructorsMap", instructorsMap);
                model.addAttribute("isWrapper", true);
            }

            // Clear the session attribute after adding to model to prevent it from reappearing on refresh
            //request.getSession().removeAttribute("parsedData");
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

    //doar pentru a testa cele endpointuri
    @PostMapping("/uploadFiles")
    public ResponseEntity<Object> handleFilesUpload(@RequestParam("file") MultipartFile file,
                                                    RedirectAttributes redirectAttributes,
                                                    HttpServletRequest request) {
        try {
            String contentType = file.getContentType();
            Object parsedData = null;

            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            if ("application/xml".equals(contentType) || file.getOriginalFilename().endsWith(".xml")) {
                parsedData = fitnessService.parseXml(content);
            } else if ("application/json".equals(contentType) || file.getOriginalFilename().endsWith(".json")) {
                parsedData = fitnessService.parseJson(content);
            } else {
                redirectAttributes.addFlashAttribute("message", "Unsupported file type");
                return new ResponseEntity<>(parsedData, HttpStatus.OK);
            }

            request.getSession().setAttribute("parsedData", parsedData);
            redirectAttributes.addFlashAttribute("message", "File successfully uploaded and parsed");
            return new ResponseEntity<>(parsedData, HttpStatus.OK);

        } catch (IOException | JAXBException e) {
            redirectAttributes.addFlashAttribute("message", "File upload failed: " + e.getMessage());
            return new ResponseEntity<>("Nu a fost gasit", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/membri")
    public String getMembruByName(@RequestParam String name, HttpServletRequest request, Model model) {
        Object parsedData = request.getSession().getAttribute("parsedData");
        model.addAttribute("parsedData", parsedData);
        List<Membru> membri = fitnessService.findMembruByName(name, parsedData);
        if (membri != null && !membri.isEmpty()) {
            model.addAttribute("foundMembersWithName", membri);
        } else {
            model.addAttribute("searchError", "No members found with that name.");
        }

        return "upload";
    }
    @GetMapping("/echipamente")
    public String getEchipamentByType(@RequestParam String tip, HttpServletRequest request, Model model) {
        Object parsedData = request.getSession().getAttribute("parsedData");
        model.addAttribute("parsedData", parsedData);
        List<Echipament> echipamente = fitnessService.findEchipamentByType(tip, parsedData);
        if (echipamente != null && !echipamente.isEmpty()) {
            model.addAttribute("foundEchipamenteByType", echipamente);
        } else {
            model.addAttribute("searchError", "No members found with that name.");
        }

        return "upload";
    }


}
