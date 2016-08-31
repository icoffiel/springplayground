package com.nerdery.icoffiel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Configuration class for Thymeleaf.
 */
@Configuration
public class ThymeleafConfig {

    @Bean
    ClassLoaderTemplateResolver pdfTemplateResolver() {
        ClassLoaderTemplateResolver pdfTemplateResolver = new ClassLoaderTemplateResolver();
        pdfTemplateResolver.setPrefix("pdfs/");
        pdfTemplateResolver.setSuffix(".html");
        pdfTemplateResolver.setTemplateMode("HTML5");
        pdfTemplateResolver.setOrder(1);
        return pdfTemplateResolver;
    }
}
