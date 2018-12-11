package com.joelmatth.gigstream;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

public class Search {

    private final Repository repository;
    private final Config config;

    Search(Repository repository, Config config) {
        this.repository = repository;
        this.config = config;
    }

    public List<Gig> byTerms(String artist, String name, String location) {
        return repository.findByArtistContainingIgnoreCaseOrNameContainingIgnoreCaseOrLocationContainingIgnoreCase(
                artist, name, location);
    }

    public List<Gig> byTerm(String term) {
        return byTerms(term, term, term);
    }

    public Optional<Gig> load(int id) {
        Optional<Gig> gig = Optional.empty();
        List<Gig> gigs = repository.findById(id);

        if (gigs.size() == 1) {
            gig = Optional.of(gigs.get(0));
        }

        return gig;
    }

    public List<Gig> recent() {
        return repository.findTop12ByOrderByIdDesc();
    }

    public String mostCommonLocation() {
        String mostCommonLocation = config.mostCommonLocation;

        Map<String, List<Gig>> gigsByLocation = repository.findAll().stream()
                .collect(groupingBy(Gig::getLocation));

        Optional<Map.Entry<String, List<Gig>>> mostCommonLocationEntry =
                gigsByLocation.entrySet().stream()
                .max(comparingInt(e -> e.getValue().size()));

        if (mostCommonLocationEntry.isPresent()) {
            mostCommonLocation = mostCommonLocationEntry.get().getKey();
        }

        return mostCommonLocation;
    }

}
