package br.com.redhat.dto;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.List;

public class MovieList {

    private final List<Movie> movies;

    @ProtoFactory
    public MovieList(List<Movie> movies) {
        this.movies = movies;
    }

    @ProtoField(number = 1)
    public List<Movie> getMovies() {
        return movies;
    }
}

