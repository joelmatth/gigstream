package com.joelmatth.gigstream;

import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
class GigUrlFactory {

    Config config;

    GigUrl of(Gig gig) {
        return new GigUrl(config, gig);
    }

    List<GigUrl> of(List<Gig> gigs) {
        return gigs.stream()
                .map(g -> new GigUrl(config, g))
                .collect(Collectors.toList());
    }
}
