package br.com.redhat.dto;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

public class Movie {
    private final long id;
    private final String name;
    private final String director;
    private final String year;
    private final String genre;

    @ProtoFactory
    public Movie(long id, String name, String director, String year, String genre) {
        this.id = id;
        this.name = name;
        this.director = director;
        this.year = year;
        this.genre = genre;
    }

    @ProtoField(number = 1, defaultValue = "0")
    public long getId() { return id; }

    @ProtoField(number = 2)
    public String getName() { return name; }

    @ProtoField(number = 3)
    public String getDirector() { return director; }

    @ProtoField(number = 4, defaultValue = "0")
    public String getYear() { return year; }

    @ProtoField(number = 5)
    public String getGenre() { return genre; }
}