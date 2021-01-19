package com.joelmatth.gigstream.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joelmatth.gigstream.Config;
import com.joelmatth.gigstream.Gig;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DataSource {

    public static List<Gig> get(Config config) {
        try {
            URL indexUrl = UriComponentsBuilder.fromHttpUrl(config.dataUrl)
                    .pathSegment(config.indexFilename).build().toUri().toURL();

            return new ObjectMapper().readValue(indexUrl, new TypeReference<List<Gig>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

}
