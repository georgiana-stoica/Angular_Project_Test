package com.example.demo.service;

import ch.qos.logback.classic.util.LevelToSyslogSeverity;
import com.example.demo.model.Membru;
import com.example.demo.model.SalaDeFitness;
import com.example.demo.model.SalaDeFitnessWrappper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.List;
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

    public SalaDeFitness parseXml(String xmlContent) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(SalaDeFitness.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (SalaDeFitness) unmarshaller.unmarshal(new StringReader(xmlContent));
    }

    public String readJsonContent() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("static/sala_de_fitness.json");
        byte[] byteArray = StreamUtils.copyToByteArray(classPathResource.getInputStream());
        return new String(byteArray, StandardCharsets.UTF_8);
    }

    public SalaDeFitnessWrappper parseJson(String jsonContent) throws IOException {

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

    public Membru findMembruById(String id, HttpServletRequest request) {
        Object dataFromSession = request.getSession().getAttribute("parsedData");
        if (dataFromSession instanceof SalaDeFitnessWrappper) {
            SalaDeFitnessWrappper wrapper = (SalaDeFitnessWrappper) dataFromSession;
            List<Membru> membri = wrapper.getSalaDeFitness().getMembri();
            for (Membru membru : membri) {
                if (membru.getId().equals(id)) {
                    return membru;
                }
            }
        }
        return null;
    }

}


