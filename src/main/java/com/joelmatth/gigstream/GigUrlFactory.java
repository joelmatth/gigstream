package com.joelmatth.gigstream;

import java.util.List;
import java.util.stream.Collectors;

class GigUrlFactory {

    private final Config config;

    GigUrlFactory(Config config) {
        this.config = config;
    }

    GigUrl of(Gig gig) {
        return new GigUrl(config, gig);
    }

    List<GigUrl> of(List<Gig> gigs) {
        return gigs.stream()
                .map(g -> new GigUrl(config, g))
                .collect(Collectors.toList());
    }
}
