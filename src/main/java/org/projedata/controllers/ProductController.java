package org.projedata.controllers;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.projedata.models.Product;
import org.projedata.repository.ProductRepository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Product Management", description = "Endpoints for managing final products") 
public class ProductController {

    @Inject
    ProductRepository repository;

    @GET
    @Operation(summary = "List all products", description = "Returns a list of all products registered in the database.")
    @APIResponse(responseCode = "200", description = "List of products returned successfully")
    public List<Product> listAll() {
        return repository.listAll();
    }

    @POST
    @Transactional
    @Operation(summary = "Create a new product", description = "Registers a new product with name and price.")
    @APIResponse(responseCode = "201", description = "Product created successfully")
    @APIResponse(responseCode = "400", description = "Invalid input data")
    public Response create(Product product) {
        repository.persist(product);
        return Response.status(Response.Status.CREATED).entity(product).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Create or update a product", description = "Update a new product existing one based on the product ID.")
    @APIResponse(responseCode = "200", description = "Product updated successfully")
    @APIResponse(responseCode = "400", description = "Invalid input data")
    public Response update(Long id, Product product) {
        Product existingProduct = repository.findById(id);
        if (existingProduct == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        
        return Response.ok(existingProduct).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete a product", description = "Deletes a product based on the provided product ID.")
    @APIResponse(responseCode = "200", description = "Product deleted successfully")
    @APIResponse(responseCode = "404", description = "Product not found")
    public Response delete(Long id) {
        Product existingProduct = repository.findById(id);
        if (existingProduct == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        repository.deleteById(id);
        return Response.ok().build();
    }
}
