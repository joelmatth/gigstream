package com.joelmatth.gigstream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "configuration.properties", ignoreResourceNotFound = true)
public class Config {

    @Value("${title:Gigstream}")
    public String siteTitle;

    @Value("${data.url:https://example.com/store/}")
    public String gigStore;

    @Value("${video.filename.suffix:set.mp4}")
    public String videoSuffix;

    @Value("${image.filename.suffix:set.jpg}")
    public String imageSuffix;

    @Value("${thumbnail.filename.suffix:thumb.jpg}")
    public String thumbSuffix;

    @Value("${chapters.filename.suffix:chapters.vtt}")
    public String chaptersSuffix;

    @Value("${gig.list.filename:gigs.json}")
    public String gigList;

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

}
