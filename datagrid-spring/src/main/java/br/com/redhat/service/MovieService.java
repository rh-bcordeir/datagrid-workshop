package br.com.redhat.service;

import br.com.redhat.model.MovieEntity;
import br.com.redhat.model.MovieRepository;
import br.com.redhat.proto.Movie;
import br.com.redhat.proto.MovieList;
import org.infinispan.client.hotrod.RemoteCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    private static final Logger log = LoggerFactory.getLogger(MovieService.class);
    private static final String KEY_ALL = "movies:all";
    private static final String KEY_BY_ID_PREFIX = "movies:id:";

    private final MovieRepository movieRepository;
    private final RemoteCache<String, MovieList> movieListCache;
    private final RemoteCache<String, Movie> movieCache;

    public MovieService(
            MovieRepository movieRepository,
            @Qualifier("movieListCache") RemoteCache<String, MovieList> movieListCache,
            @Qualifier("movieCache") RemoteCache<String, Movie> movieCache) {
        this.movieRepository = movieRepository;
        this.movieListCache = movieListCache;
        this.movieCache = movieCache;
    }

    public MovieList listAllCached() {
        // tenta cache
        MovieList cached = movieListCache.get(KEY_ALL);
        if (cached != null) {
            log.info("\nCached MovieList\n");
            return cached;
        }

        // se nao houver no cache vai no banco
        List<MovieEntity> movies = movieRepository.findAll();
        var result = new MovieList(
                movies.stream()
                        .map(e -> new Movie(e.id, e.name, e.director, e.year, e.genre))
                        .toList()
        );

        // grava no cache
        movieListCache.put(KEY_ALL, result);

        log.info("\nDB MovieList\n");
        return result;
    }

    public Movie getById(Long id) {
        String key = KEY_BY_ID_PREFIX + id;

        Movie cached = movieCache.get(key);
        if (cached != null) {
            log.info("\nCached Movie\n");
            return cached;
        }

        MovieEntity e = movieRepository.findById(id).orElse(null);
        if (e == null) {
            return null;
        }

        Movie movie = new Movie(e.id, e.name, e.director, e.year, e.genre);
        movieCache.put(key, movie);

        log.info("\nDB Movie\n");
        return movie;
    }

    @Transactional
    public MovieEntity create(Movie movie) {
        var movieEntity = new MovieEntity();

        movieEntity.name = movie.name();
        movieEntity.director = movie.director();
        movieEntity.year = movie.year();
        movieEntity.genre = movie.genre();

        movieRepository.save(movieEntity);
        invalidateAll();

        return movieEntity;
    }

    @Transactional
    public void delete(Long id) {
        movieRepository.deleteById(id);
        invalidateAll();
    }

    public void invalidateAll() {
        movieListCache.remove(KEY_ALL);
    }
}

