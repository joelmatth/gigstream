package com.joelmatth.gigstream.data;

import com.joelmatth.gigstream.Gig;

class Matches {

    static boolean artist(Gig gig, String term) {
        return termMatch(gig, Gig::getArtist, term);
    }

    static boolean date(Gig gig, String term) {
        return termMatch(gig, g -> g.getDate().toString(), term);
    }

    static boolean location(Gig gig, String term) {
        return termMatch(gig, Gig::getLocation, term);
    }

    static boolean name(Gig gig, String term) {
        return termMatch(gig, Gig::getName, term);
    }

    static boolean termMatch(Gig gig, Getter getter, String term) {
        String value = getter.get(gig);
        if (value == null) return false;
        return value.toLowerCase().contains(term.toLowerCase());
    }

    interface Getter {
        String get(Gig gig);
    }

}
