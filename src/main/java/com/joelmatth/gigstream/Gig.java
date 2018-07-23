package com.joelmatth.gigstream;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Document
public class Gig {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    private int id;
    private String artist;
    private String location;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getYear() {
        return date.getYear();
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return date.format(formatter);
    }

    private String getUrl(String suffix) {
        return String.format("%s/%d%s", Config.GIG_STORE, id, suffix);
    }

    public String getVideoUrl() {
        return UrlBuilder.video(this).toString();
    }

    public String getImageUrl() {
        return UrlBuilder.image(this).toString();
    }

    public String getThumbUrl() {
        return UrlBuilder.thumb(this).toString();
    }

    public String getChaptersUrl() {
        return UrlBuilder.chapters(this).toString();
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getFormattedName() {
        return null == name ? artist + " at " + location : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Gig " + date + " " + artist;
    }
}
