package br.com.redhat.resource;

import br.com.redhat.model.MovieEntity;
import br.com.redhat.dto.Movie;
import br.com.redhat.dto.MovieList;
import br.com.redhat.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    private final MovieService service;

    public MovieResource(MovieService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<MovieList> listAll() {
        MovieList result = service.listAllCached();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getById(@PathVariable Long id) {
        Movie movie = service.getById(id);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movie);
    }

    @PostMapping
    public ResponseEntity<MovieEntity> create(@RequestBody Movie movie) {
        MovieEntity created = service.create(movie);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

