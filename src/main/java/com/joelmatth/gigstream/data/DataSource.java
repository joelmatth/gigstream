package com.joelmatth.gigstream.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joelmatth.gigstream.Config;
import com.joelmatth.gigstream.Gig;
import com.joelmatth.gigstream.UrlFactory;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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
        URL listUrl = UrlFactory.get(config.dataUrl, config.indexFilename);

        try (Scanner scanner = new Scanner(listUrl.openStream(), StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            String json = scanner.hasNext() ? scanner.next() : "";
            ObjectMapper mapper = new ObjectMapper();
            gigs = mapper.readValue(json, new TypeReference<List<Gig>>(){});
        } catch (IOException e) {
            log.error("Error: Failed to load list from {} - check configuration", listUrl);
            System.exit(2);
        }

        return gigs;
    }

}
