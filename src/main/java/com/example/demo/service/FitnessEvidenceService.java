package com.example.demo.service;

import com.example.demo.model.SalaDeFitness;
import com.example.demo.model.SalaDeFitnessWrappper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Service
public class FitnessEvidenceService {

//    @Value("fitness.xml.path")
//    String xmlFile;
    //private static final Logger logger = (Logger) LoggerFactory.getLogger(SalaDeFitnessWrappper.class);

    public String readXmlContent() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("static/evidenta-fitness.xml");
        byte[] byteArray = StreamUtils.copyToByteArray(classPathResource.getInputStream());
        return new String(byteArray, StandardCharsets.UTF_8);
    }

    public SalaDeFitness parseXml(String xmlContent) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(SalaDeFitness.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (SalaDeFitness) unmarshaller.unmarshal(new StringReader(xmlContent));
    }

    public SalaDeFitnessWrappper parseJson() throws IOException {

        ClassPathResource classPathResource = new ClassPathResource("static/sala_de_fitness.json");
        byte[] byteArray = StreamUtils.copyToByteArray(classPathResource.getInputStream());
        String jsonContent = new String(byteArray, StandardCharsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();
        SalaDeFitnessWrappper salaDeFitnessWrapper = new SalaDeFitnessWrappper();
        try {
            salaDeFitnessWrapper = objectMapper.readValue(jsonContent, SalaDeFitnessWrappper.class);
//            logger.info(String.valueOf(salaDeFitnessWrapper));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return salaDeFitnessWrapper;
    }

}


