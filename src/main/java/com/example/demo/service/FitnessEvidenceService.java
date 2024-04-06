package com.example.demo.service;

import com.example.demo.model.SalaDeFitness;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

@Service
public class FitnessEvidenceService {

//    @Value("fitness.xml.path")
//    String xmlFile;

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


}


