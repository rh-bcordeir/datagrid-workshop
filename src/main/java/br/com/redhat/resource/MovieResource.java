package br.com.redhat.resource;

import br.com.redhat.proto.Movie;
import br.com.redhat.proto.MovieList;
import br.com.redhat.service.MovieService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;


@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

    @Inject
    MovieService service;

    @GET
    public Response listAll() {
        MovieList result = service.listAllCached();
        return Response.ok(result).build();
    }

    @POST
    @Transactional
    public Response create(Movie movie) {
        Movie created = service.create(movie);
        return Response.ok(created).build();
    }


    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}