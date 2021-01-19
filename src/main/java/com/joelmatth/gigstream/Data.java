package com.joelmatth.gigstream;

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

    Optional<Gig> byId(int id) {
        return gigs.stream()
                .filter(gig -> gig.getId() == id)
                .findFirst();
    }

    List<Gig> byDateDescending() {
        return gigs.stream()
                .sorted(this::dateDescending)
                .collect(toList());
    }

    List<Gig> byMostCommonLocation() {
        return gigs.stream()
                .filter(gig -> locationMatch(gig, mostCommonLocation()))
                .sorted(this::dateDescending)
                .collect(toList());
    }

    List<Gig> byRecentlyAdded() {
        return gigs.stream()
                .sorted(this::idDescending)
                .limit(NUM_RECENT)
                .collect(toList());
    }

    List<Gig> search(String term) {
        return gigs.stream()
                .filter(gig ->
                            artistMatch(gig, term) ||
                            dateMatch(gig, term) ||
                            locationMatch(gig, term) ||
                            nameMatch(gig, term))
                .sorted(this::dateDescending)
                .collect(toList());
    }

    boolean artistMatch(Gig gig, String term) {
        return termMatch(gig, Gig::getArtist, term);
    }

    boolean dateMatch(Gig gig, String term) {
        return termMatch(gig, g -> g.getDate().toString(), term);
    }

    boolean locationMatch(Gig gig, String term) {
        return termMatch(gig, Gig::getLocation, term);
    }

    boolean nameMatch(Gig gig, String term) {
        return termMatch(gig, Gig::getName, term);
    }

    boolean termMatch(Gig gig, Getter getter, String term) {
        String value = getter.get(gig);
        if (value == null) return false;
        return value.toLowerCase().contains(term.toLowerCase());
    }

    int dateDescending(Gig left, Gig right) {
        return right.getDate().compareTo(left.getDate());
    }

    int idAscending(Gig left, Gig right) {
        return left.getId() - right.getId();
    }

    int idDescending(Gig left, Gig right) {
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

    interface Getter {
        String get(Gig gig);
    }

}
