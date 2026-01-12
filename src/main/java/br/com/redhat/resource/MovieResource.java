package br.com.redhat.resource;

import br.com.redhat.proto.Movie;
import io.quarkus.infinispan.client.Remote;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.infinispan.client.hotrod.RemoteCache;

import java.util.concurrent.CompletionStage;

@Path("/movies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource {

    @Inject
    @Remote("movies")
    RemoteCache<String, Movie> movies; // cache remoto "movies"

    @POST
    @Path("/{id}")
    public CompletionStage<String> put(@PathParam("id") String id, Movie movie) {
        return movies.putAsync(id, movie)
                .thenApply(prev -> "OK")
                .exceptionally(Throwable::getMessage);
    }

    @GET
    @Path("/{id}")
    public CompletionStage<Movie> get(@PathParam("id") String id) {
        return movies.getAsync(id);
    }
}
