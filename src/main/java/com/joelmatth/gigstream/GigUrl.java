package com.joelmatth.gigstream;

import java.net.MalformedURLException;
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
        return buildFromGig(config.videoSuffix);
    }

    public URL getImage() {
        return buildFromGig(config.imageSuffix);
    }

    public URL getThumb() {
        return buildFromGig(config.thumbSuffix);
    }

    public URL getChapters() {
        return buildFromGig(config.chaptersSuffix);
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
        return build(String.format("%04d_%s", gig.getId(), suffix));
    }

    private URL build(String filename) {
        URL url = null;

        try {
            URL rootUrl = new URL(config.gigStore);
            url = new URL(rootUrl, filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return url;
    }
}
