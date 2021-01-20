package com.joelmatth.gigstream.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class Gig {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    private int id;
    private String artist;
    private String location;
    private String name;

    public int getYear() {
        return date.getYear();
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return date.format(formatter);
    }

    public String getFormattedName() {
        return null == name
                ? artist + " at " + location
                : name;
    }

    public String objectUrl(String dataUrl, String object) {
        return String.format("%s/%d_%s", dataUrl, id, object);
    }

}
