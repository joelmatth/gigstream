package com.joelmatth.gigstream;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class Config {

    public String dataUrl = envVarOrDefault("DATA_URL", "https://raw.githubusercontent.com/joelmatth/gigstream/master/example");
    public String indexFilename = envVarOrDefault("INDEX_FILENAME", "index.json");
    public String siteTitle = envVarOrDefault("SITE_TITLE", "Gigstream");

    public boolean hasFavicon152 = resourceExists("static/favicon-152.png");
    public boolean hasLogo = resourceExists("static/img/logo.png");
    public String mostCommonLocation = "";

    @Bean
    public GigUrlFactory gigUrlFactory(Config config) {
        return new GigUrlFactory(config);
    }

    @Bean
    Search search(Repository repository, Config config) {
        return new Search(repository, config);
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

    private boolean resourceExists(String location) {
        return new ClassPathResource(location).exists();
    }

    private String envVarOrDefault(String envVar, String defaultValue) {
        String value = System.getenv(envVar);
        return value == null ? defaultValue : value;
    }
}
