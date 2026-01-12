package br.com.redhat.service;

import br.com.redhat.model.MovieEntity;
import br.com.redhat.proto.Movie;
import br.com.redhat.proto.MovieList;
import io.quarkus.infinispan.client.Remote;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.infinispan.client.hotrod.RemoteCache;

import java.util.List;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class MovieService {

    private static final String KEY_ALL = "movies:all";
    private static final long TTL_MINUTES = 5;

    @Inject
    @Remote("movies")
    RemoteCache<String, MovieList> movieListCache;

    public MovieList listAllCached() {

        // tenta cache
        MovieList cached = movieListCache.get(KEY_ALL);
        if (cached != null) {
            Log.info("\nCached MovieList\n");
            return cached;
        }

        // se nao houver no cache vai no banco
        List<MovieEntity> movies = MovieEntity.listAll();
        var result = new MovieList(
                movies.stream()
                        .map(e -> new Movie(e.id, e.name, e.director, e.year, e.genre))
                        .toList()
        );

        // grava no cache
        movieListCache.put(KEY_ALL, result, TTL_MINUTES, TimeUnit.MINUTES);

        Log.info("\nDB MovieList\n");
        return result;
    }

    public Movie create(Movie movie) {
        var movieEntity = new MovieEntity();

        movieEntity.name = movie.name();
        movieEntity.director = movie.director();
        movieEntity.year = movie.year();
        movieEntity.genre = movie.genre();

        MovieEntity.persist(movieEntity);
        invalidateAll();

        return movie;
    }

    public void delete(Long id) {
        MovieEntity.deleteById(id);
        invalidateAll();
    }

    public void invalidateAll() {
        movieListCache.remove(KEY_ALL);
    }
}
