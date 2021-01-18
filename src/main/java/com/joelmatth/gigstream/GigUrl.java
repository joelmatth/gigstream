package com.joelmatth.gigstream;

import lombok.Value;

import java.net.URL;
import java.time.format.DateTimeFormatter;

@Value
class GigUrl {

    Config config;
    Gig gig;

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
