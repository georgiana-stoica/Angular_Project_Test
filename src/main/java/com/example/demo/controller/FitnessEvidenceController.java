package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.FitnessEvidenceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/fitness")
public class FitnessEvidenceController {

    @Autowired
    private FitnessEvidenceService fitnessService;
    @GetMapping("/")
    public String getFitnessData(Model model) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ClassPathResource("static/sala_de_fitness.xml").getInputStream());

        model.addAttribute("xmlSource", document);
        return "sala_de_fitness";
    }

    @GetMapping("/upload")
    public String loadUploadFormAndDisplayData(Model model, HttpServletRequest request) {
        Object parsedData = request.getSession().getAttribute("parsedData");
        if (parsedData != null) {
            model.addAttribute("parsedData", parsedData);

            if (parsedData instanceof SalaDeFitness) {
                SalaDeFitness salaDeFitness = (SalaDeFitness) parsedData;
                Map<String, String> classesMap = new HashMap<>();
                Map<String, String> instructorsMap = new HashMap<>();
                Map<String, String> programareMap = new HashMap<>();
                for (Membru membru : salaDeFitness.getMembri()) {
                    StringBuilder classesBuilder = new StringBuilder();
                    StringBuilder instructorsBuilder = new StringBuilder();
                    StringBuilder programareBuilder = new StringBuilder();
                    for (Rezervare rezervare : membru.getRezervari()) {
                        try {
                            Clasa clasa = fitnessService.getClasaById(rezervare.getClasa_id());
                            if (!classesBuilder.isEmpty()) {
                                classesBuilder.append(", ");
                                instructorsBuilder.append(", ");
                                programareBuilder.append(", ");
                            }
                            classesBuilder.append(clasa.getTitlu());
                            instructorsBuilder.append(clasa.getInstructor());
                            programareBuilder.append(clasa.getProgramare());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    classesMap.put(membru.getId(), classesBuilder.toString());
                    instructorsMap.put(membru.getId(), instructorsBuilder.toString());
                    programareMap.put(membru.getId(), programareBuilder.toString());
                }
                model.addAttribute("classesMap", classesMap);
                model.addAttribute("instructorsMap", instructorsMap);
                model.addAttribute("programmeMap", programareMap);
                model.addAttribute("isNotWrapper", true);

            } else if (parsedData instanceof SalaDeFitnessWrapper) {
                SalaDeFitnessWrapper wrapper = (SalaDeFitnessWrapper) parsedData;
                Map<String, String> classesMap = new HashMap<>();
                Map<String, String> instructorsMap = new HashMap<>();
                Map<String, String> programareMap = new HashMap<>();
                for (Membru membru : wrapper.getSalaDeFitness().getMembri()) {
                    StringBuilder classesBuilder = new StringBuilder();
                    StringBuilder instructorsBuilder = new StringBuilder();
                    StringBuilder programareBuilder = new StringBuilder();
                    for (Rezervare rezervare : membru.getRezervari()) {
                        try {
                            Clasa clasa = fitnessService.getClasaById(rezervare.getClasa_id());
                            if (!classesBuilder.isEmpty()) {
                                classesBuilder.append(", ");
                                instructorsBuilder.append(", ");
                                programareBuilder.append(", ");
                            }
                            classesBuilder.append(clasa.getTitlu());
                            instructorsBuilder.append(clasa.getInstructor());
                            programareBuilder.append(clasa.getProgramare());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    classesMap.put(membru.getId(), classesBuilder.toString());
                    instructorsMap.put(membru.getId(), instructorsBuilder.toString());
                    programareMap.put(membru.getId(), programareBuilder.toString());
                }
                model.addAttribute("classesMap", classesMap);
                model.addAttribute("instructorsMap", instructorsMap);
                model.addAttribute("programmeMap", programareMap);
                model.addAttribute("isWrapper", true);
            }
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

    //doar pentru a testa cele 2 endpointuri
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
