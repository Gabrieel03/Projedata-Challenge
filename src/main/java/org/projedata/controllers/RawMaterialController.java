package org.projedata.controllers;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.projedata.models.RawMaterial;
import org.projedata.services.RawMaterialService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Raw Material Management", description = "Endpoints for managing inventory raw materials")
public class RawMaterialController {

    @Inject
    RawMaterialService service;

    @GET
    @Operation(summary = "List all raw materials")
    @APIResponse(responseCode = "200", description = "List returned successfully")
    public List<RawMaterial> listAll() {
        return service.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Find raw material by ID")
    @APIResponse(responseCode = "200", description = "Raw material found")
    @APIResponse(responseCode = "404", description = "Raw material not found")
    public Response findById(@PathParam("id") Long id) {
        RawMaterial rawMaterial = service.findById(id);
        return Response.ok(rawMaterial).build();
    }

    @POST
    @Operation(summary = "Create a new raw material")
    @APIResponse(responseCode = "201", description = "Raw material created successfully")
    public Response create(RawMaterial rawMaterial) {
        RawMaterial createdMaterial = service.create(rawMaterial);
        return Response.status(Response.Status.CREATED).entity(createdMaterial).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update a raw material")
    @APIResponse(responseCode = "200", description = "Raw material updated successfully")
    @APIResponse(responseCode = "404", description = "Raw material not found")
    public Response update(@PathParam("id") Long id, RawMaterial rawMaterial) {
        RawMaterial updatedMaterial = service.update(id, rawMaterial);
        return Response.ok(updatedMaterial).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a raw material")
    @APIResponse(responseCode = "204", description = "Raw material deleted successfully")
    @APIResponse(responseCode = "404", description = "Raw material not found")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
