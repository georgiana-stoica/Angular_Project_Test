package com.example.demo.service;

import com.example.demo.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FitnessEvidenceService {

    public String readXmlContent() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("sala_de_fitness.xml");
        byte[] byteArray = StreamUtils.copyToByteArray(classPathResource.getInputStream());
        return new String(byteArray, StandardCharsets.UTF_8);
    }

    public SalaDeFitness parseXml(String xmlContent) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(SalaDeFitness.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return ((SalaDeFitness) unmarshaller.unmarshal(new StringReader(xmlContent)));

    }

    public String readJsonContent() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("static/sala_de_fitness.json");
        byte[] byteArray = StreamUtils.copyToByteArray(classPathResource.getInputStream());
        return new String(byteArray, StandardCharsets.UTF_8);
    }

    public Clasa getClasaById(String clasaId) throws IOException, JAXBException {
        SalaDeFitness salaDeFitness = parseXml(readXmlContent());
        return salaDeFitness.getClase().stream()
                .filter(clasa -> clasa.getId().equals(clasaId))
                .findFirst()
                .orElse(null);
    }

    public SalaDeFitnessWrapper parseJson(String jsonContent) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        SalaDeFitnessWrapper salaDeFitnessWrapper = new SalaDeFitnessWrapper();
        try {
            salaDeFitnessWrapper = objectMapper.readValue(jsonContent, SalaDeFitnessWrapper.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salaDeFitnessWrapper;
    }

    public List<Membru> findMembruByName(String name, Object dataFromSession) {
        if (dataFromSession instanceof SalaDeFitnessWrapper) {
            SalaDeFitnessWrapper wrapper = (SalaDeFitnessWrapper) dataFromSession;
            return wrapper.getSalaDeFitness().getMembri().stream()
                    .filter(membru -> membru.getNume().contains(name))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<Echipament> findEchipamentByType(String tip, Object dataFromSession) {
        if (dataFromSession instanceof SalaDeFitnessWrapper) {
            SalaDeFitnessWrapper wrapper = (SalaDeFitnessWrapper) dataFromSession;
            return wrapper.getSalaDeFitness().getEchipamente().stream()
                    .filter(echipament -> echipament.getTip().contains(tip))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}


