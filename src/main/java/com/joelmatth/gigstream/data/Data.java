package com.joelmatth.gigstream.data;

import com.joelmatth.gigstream.Gig;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Value
public class Data {

    public static int NUM_RECENT = 12;

    List<Gig> gigs;

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
                .filter(gig -> Matches.location(gig, mostCommonLocation()))
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
                            Matches.artist(gig, term) ||
                            Matches.date(gig, term) ||
                            Matches.location(gig, term) ||
                            Matches.name(gig, term))
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

}
