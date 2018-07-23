package com.joelmatth.gigstream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LayoutDialect {

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

}

