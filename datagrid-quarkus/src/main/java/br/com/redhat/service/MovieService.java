package br.com.redhat.service;

import br.com.redhat.model.MovieEntity;
import br.com.redhat.proto.Movie;
import br.com.redhat.proto.MovieList;
import io.quarkus.infinispan.client.Remote;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.infinispan.client.hotrod.RemoteCache;

import java.util.List;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class MovieService {

    private static final String KEY_ALL = "movies:all";
    private static final String KEY_BY_ID_PREFIX = "movies:id:";

    @Inject
    @Remote("movies")
    RemoteCache<String, MovieList> movieListCache;

    @Inject
    @Remote("movies")
    RemoteCache<String, Movie> movieCache;

    public MovieList listAllCached() {

        // tenta cache
        MovieList cached = movieListCache.get(KEY_ALL);
        if (cached != null) {
            Log.info("\nCached MovieList\n");
            return cached;
        }

        // se nao houver no cache vai no banco
        List<MovieEntity> movies = MovieEntity.listAll(Sort.by("id"));
        var result = new MovieList(
                movies.stream()
                        .map(e -> new Movie(e.id, e.name, e.director, e.year, e.genre))
                        .toList()
        );

        // grava no cache
        movieListCache.put(KEY_ALL, result);

        Log.info("\nDB MovieList\n");
        return result;
    }

    public Movie getById(Long id) {
        String key = KEY_BY_ID_PREFIX + id;

        Movie cached = movieCache.get(key);
        if (cached != null) {
            Log.info("\nCached Movie\n");
            return cached;
        }

        MovieEntity e = MovieEntity.findById(id);
        if (e == null) {
            return null;
        }

        Movie movie = new Movie(e.id, e.name, e.director, e.year, e.genre);
        movieCache.put(key, movie);

        Log.info("\nDB Movie\n");
        return movie;
    }

    public MovieEntity create(Movie movie) {
        var movieEntity = new MovieEntity();

        movieEntity.name = movie.name();
        movieEntity.director = movie.director();
        movieEntity.year = movie.year();
        movieEntity.genre = movie.genre();

        MovieEntity.persist(movieEntity);
        invalidateAll();

        return movieEntity;
    }

    public void delete(Long id) {
        MovieEntity.deleteById(id);
        invalidateAll();
        movieCache.remove(KEY_BY_ID_PREFIX + id);
    }

    public void invalidateAll() {
        movieListCache.remove(KEY_ALL);
    }
}
