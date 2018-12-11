package com.joelmatth.gigstream;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

class GigUrl {

    final Gig gig;

    GigUrl(Gig gig) {
        this.gig = gig;
    }

    static List<GigUrl> of(List<Gig> gigs) {
        return gigs.stream()
                .map(GigUrl::new)
                .collect(Collectors.toList());
    }

    public URL getVideo() {
        return buildFromGig(Config.VIDEO_SUFFIX);
    }

    public URL getImage() {
        return buildFromGig(Config.IMAGE_SUFFIX);
    }

    public URL getThumb() {
        return buildFromGig(Config.THUMB_SUFFIX);
    }

    public URL getChapters() {
        return buildFromGig(Config.CHAPTERS_SUFFIX);
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
            URL rootUrl = new URL(Config.GIG_STORE);
            url = new URL(rootUrl, filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return url;
    }
}
