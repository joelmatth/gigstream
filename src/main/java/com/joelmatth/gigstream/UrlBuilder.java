package com.joelmatth.gigstream;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlBuilder {

    public static URL gigList() {
        return build(Config.GIG_LIST);
    }

    public static URL video(Gig gig) {
        return buildFromGig(gig, Config.VIDEO_SUFFIX);
    }

    public static URL image(Gig gig) {
        return buildFromGig(gig, Config.IMAGE_SUFFIX);
    }
    public static URL thumb(Gig gig) {
        return buildFromGig(gig, Config.THUMB_SUFFIX);
    }

    public static URL chapters(Gig gig) {
        return buildFromGig(gig, Config.CHAPTERS_SUFFIX);
    }

    private static URL buildFromGig(Gig gig, String suffix) {
        return build(String.format("%04d_%s", gig.getId(), suffix));
    }

    private static URL build(String suffix) {
        URL url = null;

        try {
            url = new URL(Config.GIG_STORE + "/" + suffix);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return url;
    }
}
