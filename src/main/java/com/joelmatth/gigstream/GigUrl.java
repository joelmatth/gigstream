package com.joelmatth.gigstream;

import java.net.URL;
import java.time.format.DateTimeFormatter;

class GigUrl {

    private final Config config;
    private final Gig gig;

    GigUrl(Config config, Gig gig) {
        this.config = config;
        this.gig = gig;
    }

    public URL getVideo() {
        return buildFromGig("set");
    }

    public URL getImage() {
        return buildFromGig("image");
    }

    public URL getThumb() {
        return buildFromGig("thumb");
    }

    public URL getTracks() {
        return buildFromGig("tracks");
    }

    public String getFormattedName() {
        return null == gig.getName()
                ? gig.getArtist() + " at " + gig.getLocation()
                : gig.getName();
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return gig.getDate().format(formatter);
    }

    public Gig getGig() {
        return gig;
    }

    private URL buildFromGig(String suffix) {
        String path = String.format("%d_%s", gig.getId(), suffix);
        return UrlFactory.get(config.gigStore, path);
    }

}
