package com.joelmatth.gigstream.data;

import com.joelmatth.gigstream.model.Gig;
import com.joelmatth.gigstream.service.GigService;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Repository {

    public static final int NUM_RECENT = 12;
    public static final long RETRIEVAL_DELAY = 10 * 60 * 1000; // Ten minutes

    private final GigService gigService;
    private List<Gig> gigs = new ArrayList<>();

    public Repository(GigService gigService) {
        this.gigService = gigService;
    }

    public Optional<Gig> byId(int id) {
        return gigs.stream()
                .filter(gig -> gig.getId() == id)
                .findFirst();
    }

    public List<Gig> byDateDescending() {
        return gigs.stream()
                .sorted(this::dateDescending)
                .collect(toList());
    }

    public List<Gig> byMostCommonLocation() {
        return gigs.stream()
                .filter(gig -> GigMatches.location(gig, mostCommonLocation()))
                .sorted(this::dateDescending)
                .collect(toList());
    }

    public List<Gig> byRecentlyAdded() {
        return gigs.stream()
                .sorted(this::idDescending)
                .limit(NUM_RECENT)
                .collect(toList());
    }

    public List<Gig> search(String term) {
        return gigs.stream()
                .filter(gig ->
                            GigMatches.artist(gig, term) ||
                            GigMatches.date(gig, term) ||
                            GigMatches.location(gig, term) ||
                            GigMatches.name(gig, term))
                .sorted(this::dateDescending)
                .collect(toList());
    }

    public int dateDescending(Gig left, Gig right) {
        return right.getDate().compareTo(left.getDate());
    }

    public int idAscending(Gig left, Gig right) {
        return left.getId() - right.getId();
    }

    public int idDescending(Gig left, Gig right) {
        return -idAscending(left, right);
    }

    public String mostCommonLocation() {
        Map<String, List<Gig>> gigsByLocation = gigs.stream()
                .collect(groupingBy(Gig::getLocation));

        Optional<Map.Entry<String, List<Gig>>> mostCommonLocationEntry =
                gigsByLocation.entrySet().stream()
                        .max(comparingInt(e -> e.getValue().size()));

        if (mostCommonLocationEntry.isPresent()) {
            return mostCommonLocationEntry.get().getKey();
        }

        return "";
    }

    @Scheduled(fixedDelay = RETRIEVAL_DELAY)
    private void retrieveGigs() {
        List<Gig> gigs = gigService.getGigs();
        if (gigs != null) this.gigs = gigs;
    }

}
