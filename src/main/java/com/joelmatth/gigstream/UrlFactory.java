package com.joelmatth.gigstream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class UrlFactory {

    public static URL get(String root, String... segments) {
        try {
            return UriComponentsBuilder.fromHttpUrl(root).pathSegment(segments).build().toUri().toURL();
        } catch (MalformedURLException | IllegalArgumentException e) {
            log.error("Error: {} - check configuration", e.getMessage());
            System.exit(1);
        }

        return null;
    }

}
