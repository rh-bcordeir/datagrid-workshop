package br.com.redhat.service;

import br.com.redhat.model.MovieEntity;
import br.com.redhat.model.MovieRepository;
import br.com.redhat.dto.Movie;
import br.com.redhat.dto.MovieList;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    private static final String KEY_ALL = "movies:all";
    private static final String KEY_BY_ID_PREFIX = "movies:id:";

    @Autowired
    private MovieRepository movieRepository;

    private final RemoteCache<String, MovieList> movieListCache;
    private final RemoteCache<String, Movie> movieCache;

    public MovieService(RemoteCacheManager cacheManager) {
        this.movieListCache = cacheManager.getCache("movies");
        this.movieCache = cacheManager.getCache("movies");
    }

    @Transactional
    public MovieList listAllCached() {
        MovieList cached = movieListCache.get(KEY_ALL);
        if (cached != null) {
            log.info("Cached MovieList");
            return cached;
        }

        List<MovieEntity> movies = movieRepository.findAll(Sort.by("id"));
        MovieList result = new MovieList(
                movies.stream()
                        .map(e -> new Movie(e.getId(), e.getName(), e.getDirector(), e.getYear(), e.getGenre()))
                        .toList()
        );

        movieListCache.put(KEY_ALL, result);

        log.info("DB MovieList");
        return result;
    }

    @Transactional
    public Movie getById(Long id) {
        String key = KEY_BY_ID_PREFIX + id;

        Movie cached = movieCache.get(key);
        if (cached != null) {
            log.info("Cached Movie");
            return cached;
        }

        return movieRepository.findById(id)
                .map(e -> {
                    Movie movie = new Movie(e.getId(), e.getName(), e.getDirector(), e.getYear(), e.getGenre());
                    movieCache.put(key, movie);
                    log.info("DB Movie");
                    return movie;
                })
                .orElse(null);
    }

    @Transactional
    public MovieEntity create(Movie movie) {
        MovieEntity e = new MovieEntity();
        e.setName(movie.getName());
        e.setDirector(movie.getDirector());
        e.setYear(movie.getYear());
        e.setGenre(movie.getGenre());

        MovieEntity saved = movieRepository.save(e);

        invalidateAll();
        return saved;
    }

    @Transactional
    public void delete(Long id) {
        movieRepository.deleteById(id);

        invalidateAll();
        movieCache.remove(KEY_BY_ID_PREFIX + id);
    }

    public void invalidateAll() {
        movieListCache.remove(KEY_ALL);
    }
}

