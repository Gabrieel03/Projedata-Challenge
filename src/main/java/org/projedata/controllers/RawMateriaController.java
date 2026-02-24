package org.projedata.controllers;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.projedata.models.RawMaterial;
import org.projedata.repository.RawMaterialRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
public class RawMateriaController {

    @Inject
    RawMaterialRepository repository;

    @GET
    @Operation(summary = "List all raw materials", description = "Returns a list of all raw materials currently in stock.")
    @APIResponse(responseCode = "200", description = "List of raw materials returned successfully")
    public List<RawMaterial> listAll() {
        return repository.listAll();
    }

    @POST
    @Transactional
    @Operation(summary = "Create a new raw material", description = "Registers a new raw material with its name and stock quantity.")
    @APIResponse(responseCode = "201", description = "Raw material created successfully")
    @APIResponse(responseCode = "400", description = "Invalid input data")
    public Response create(RawMaterial rawMaterial) {
        repository.persist(rawMaterial);
        return Response.status(Response.Status.CREATED).entity(rawMaterial).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Update a raw material", description = "Updates an existing raw material's details based on the provided ID.")
    @APIResponse(responseCode = "200", description = "Raw material updated successfully")
    @APIResponse(responseCode = "404", description = "Raw material not found")
    public Response update(@PathParam("id") Long id, RawMaterial rawMaterial) {
        RawMaterial existingMaterial = repository.findById(id);

        if (existingMaterial == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        existingMaterial.setName(rawMaterial.getName());
        existingMaterial.setStockQuantity(rawMaterial.getStockQuantity());

        return Response.ok(existingMaterial).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete a raw material", description = "Deletes a raw material based on the provided ID.")
    @APIResponse(responseCode = "200", description = "Raw material deleted successfully")
    @APIResponse(responseCode = "404", description = "Raw material not found")
    public Response delete(@PathParam("id") Long id) {
        RawMaterial existingMaterial = repository.findById(id);

        if (existingMaterial == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        repository.deleteById(id);
        return Response.ok().build();
    }
}
