package com.joelmatth.gigstream.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joelmatth.gigstream.Config;
import com.joelmatth.gigstream.Gig;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Value
@Slf4j
public class DataSource {

    Config config;

    public List<Gig> get() {
        List<Gig> gigs = new ArrayList<>();
        URL indexUrl = url(config.dataUrl, config.indexFilename);

        try (Scanner scanner = new Scanner(indexUrl.openStream(), StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            String json = scanner.hasNext() ? scanner.next() : "";
            ObjectMapper mapper = new ObjectMapper();
            gigs = mapper.readValue(json, new TypeReference<List<Gig>>(){});
        } catch (IOException e) {
            log.error("Error: Failed to load list from {} - check configuration", indexUrl);
            System.exit(2);
        }

        return gigs;
    }

    public static URL url(String root, String... segments) {
        try {
            return UriComponentsBuilder.fromHttpUrl(root).pathSegment(segments).build().toUri().toURL();
        } catch (MalformedURLException | IllegalArgumentException e) {
            log.error("Error: {} - check configuration", e.getMessage());
            System.exit(1);
        }

        return null;
    }

}
