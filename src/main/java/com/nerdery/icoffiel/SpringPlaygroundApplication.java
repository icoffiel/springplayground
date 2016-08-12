package com.nerdery.icoffiel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class SpringPlaygroundApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringPlaygroundApplication.class);
    }

    public static void main(String[] args) {
		SpringApplication.run(SpringPlaygroundApplication.class, args);
	}
}
