package org.projedata.controllers;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.projedata.models.Product;
import org.projedata.services.ProductService;

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

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Product Management", description = "Endpoints for managing final products") 
public class ProductController {

    @Inject
    ProductService service;

    @GET
    @Operation(summary = "List all products")
    @APIResponse(responseCode = "200", description = "List of products returned successfully")
    public List<Product> listAll() {
        return service.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Find product by ID")
    @APIResponse(responseCode = "200", description = "Product found")
    @APIResponse(responseCode = "404", description = "Product not found")
    public Response findById(@PathParam("id") Long id) {
        Product product = service.findById(id);
        return Response.ok(product).build();
    }

    @POST
    @Operation(summary = "Create a new product")
    @APIResponse(responseCode = "201", description = "Product created successfully")
    public Response create(Product product) {
        Product createdProduct = service.create(product);
        return Response.status(Response.Status.CREATED).entity(createdProduct).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update a product")
    @APIResponse(responseCode = "200", description = "Product updated successfully")
    @APIResponse(responseCode = "404", description = "Product not found")
    public Response update(@PathParam("id") Long id, Product product) {
        Product updatedProduct = service.update(id, product);
        return Response.ok(updatedProduct).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a product")
    @APIResponse(responseCode = "204", description = "Product deleted successfully")
    @APIResponse(responseCode = "404", description = "Product not found")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
