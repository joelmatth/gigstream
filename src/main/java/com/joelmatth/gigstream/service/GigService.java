package com.joelmatth.gigstream.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joelmatth.gigstream.Config;
import com.joelmatth.gigstream.model.Gig;
import lombok.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Value
public class GigService {

    Config config;

    public List<Gig> getGigs() {
        try {
            URL indexUrl = UriComponentsBuilder.fromHttpUrl(config.dataUrl)
                    .pathSegment(config.indexFilename).build().toUri().toURL();

            return new ObjectMapper().readValue(indexUrl, new TypeReference<List<Gig>>(){});
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

}
