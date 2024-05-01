package com.example.demo.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.xslt.XsltViewResolver;

@Configuration
public class MvcConfiguration {

    @Bean
    public ViewResolver xsltViewResolver() {
        XsltViewResolver resolver = new XsltViewResolver();
        resolver.setOrder(1);
        resolver.setSourceKey("xmlSource");
        resolver.setViewClass(org.springframework.web.servlet.view.xslt.XsltView.class);
        resolver.setPrefix("classpath:/");
        resolver.setSuffix(".xsl");
        resolver.setViewNames("fitness_sala");
        return resolver;
    }

}
