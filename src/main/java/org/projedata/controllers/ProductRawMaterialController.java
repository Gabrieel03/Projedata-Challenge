package org.projedata.controllers;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.projedata.dto.ProductRawMaterialDTO;
import org.projedata.models.ProductRawMaterial;
import org.projedata.services.ProductRawMaterialService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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

@Path("/product-recipes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Product Recipe Management", description = "Endpoints for linking raw materials to products")
public class ProductRawMaterialController {

   @Inject
    ProductRawMaterialService service;

    @GET
    @Operation(summary = "List all recipes")
    public List<ProductRawMaterial> listAll() {
        return service.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get recipe by ID", description = "Returns a specific product-to-raw-material association by its ID.")
    public Response findById(@PathParam("id") Long id) {
        ProductRawMaterial recipe = service.findById(id);
        return Response.ok(recipe).build();
    }

    @GET
    @Path("/product/{productId}")
    @Operation(summary = "List recipes by Product ID", description = "Returns all raw materials associated with a specific product.")
    public List<ProductRawMaterial> listByProductId(@PathParam("productId") Long productId) {
        return service.listByProductId(productId);
    }

    @POST
    @Operation(summary = "Associate raw material to a product")
    public Response create(@Valid ProductRawMaterialDTO dto) {
        ProductRawMaterial createdRecipe = service.create(dto);
        return Response.status(Response.Status.CREATED).entity(createdRecipe).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update an association")
    public Response update(@PathParam("id") Long id, @Valid ProductRawMaterialDTO dto) {
        ProductRawMaterial updatedRecipe = service.update(id, dto);
        return Response.ok(updatedRecipe).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Remove an association")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
