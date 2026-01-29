package com.example.resources;

import com.example.entity.Institution;
import com.example.service.InstitutionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/institutions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name="Institution Resource", description = "APIs for managing institutions")
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class InstitutionResource {

    private final InstitutionService institutionService;

    @GET
    public Response getAllInstitutions(@QueryParam("shardKey") String shardKey) {
        List<Institution> institutions = institutionService.getAllInstitutions(shardKey);
        return Response.ok(institutions).build();
    }

    @GET
    @Path("/{id}")
    public Response getInstitutionById(@QueryParam("shardKey") String shardKey,
                                       @PathParam("id") Long id) {
        return institutionService.getInstitutionById(shardKey, id)
                .map(institution -> Response.ok(institution).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createInstitution(@QueryParam("shardKey") String shardKey,
                                      Institution institution) {
        institutionService.createInstitution(shardKey, institution);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateInstitution(@QueryParam("shardKey") String shardKey,
                                      @PathParam("id") Long id,
                                      Institution institution) {
        institutionService.updateInstitution(shardKey, id, institution);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}/exists")
    public Response institutionExists(@QueryParam("shardKey") String shardKey,
                                      @PathParam("id") Long id) {
        boolean exists = institutionService.institutionExists(shardKey, id);
        return Response.ok(exists).build();
    }
}
