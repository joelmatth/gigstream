package com.joelmatth.gigstream;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;

@Configuration
@PropertySource(value = "configuration.properties", ignoreResourceNotFound = true)
public class Config {

    @Value("${title:Gigstream}")
    public String siteTitle;

    @Value("${data.url:https://raw.githubusercontent.com/joelmatth/gigstream/master/example}")
    public String gigStore;

    @Value("${gig.list.filename:gigs.json}")
    public String gigList;

    public boolean hasLogo = resourceExists("static/img/logo.png");

    public boolean hasFavicon152 = resourceExists("static/favicon-152.png");

    // Placeholder location until set of gigs have been loaded
    public String mostCommonLocation = "Location";

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
}
