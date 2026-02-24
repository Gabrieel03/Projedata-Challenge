package org.projedata.controllers;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.projedata.dto.ProductRawMaterialDTO;
import org.projedata.models.Product;
import org.projedata.models.ProductRawMaterial;
import org.projedata.models.RawMaterial;
import org.projedata.repository.ProductRawMaterialRepository;
import org.projedata.repository.ProductRepository;
import org.projedata.repository.RawMaterialRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
    ProductRepository productRepository;

    @Inject
    ProductRawMaterialRepository productRawMaterialRepository;

    @Inject
    RawMaterialRepository rawMaterialRepository;

    @GET
    @Operation(summary = "List all recipes", description = "Returns all product-to-raw-material associations.")
    public List<ProductRawMaterial> listAll() {
        return productRawMaterialRepository.listAll();
    }

    @GET
    @Path("/product/{productId}")
    @Operation(summary = "List recipes by product ID", description = "Returns all raw materials associated with a specific product.")
    public List<ProductRawMaterial> listByProductId(Long productId) {
        return productRawMaterialRepository.find("product.id", productId).list();
    }

    @POST
    @Transactional
    @Operation(summary = "Associate raw material to a product", description = "Creates a link stating how much of a raw material is needed for a product.")
    @APIResponse(responseCode = "201", description = "Association created successfully")
    @APIResponse(responseCode = "404", description = "Product or Raw Material not found")
    public Response create(@Valid ProductRawMaterialDTO dto) {
        
        Product product = productRepository.findById(dto.getProductId());
        RawMaterial rawMaterial = rawMaterialRepository.findById(dto.getRawMaterialId());

        if (product == null || rawMaterial == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Product or Raw Material not found in the database.")
                    .build();
        }

        ProductRawMaterial recipe = new ProductRawMaterial();
        recipe.setProduct(product);
        recipe.setRawMaterial(rawMaterial);
        recipe.setQuantityNeeded(dto.getQuantity());

        productRawMaterialRepository.persist(recipe);

        return Response.status(Response.Status.CREATED).entity(recipe).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Update an association", description = "Updates the quantity needed or the linked product/raw material.")
    @APIResponse(responseCode = "200", description = "Association updated successfully")
    @APIResponse(responseCode = "404", description = "Association, Product, or Raw Material not found")
    public Response update(@PathParam("id") Long id, @Valid ProductRawMaterialDTO dto) {
        
        ProductRawMaterial existingRecipe = productRawMaterialRepository.findById(id);
        if (existingRecipe == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Recipe not found in the database.")
                    .build();
        }

        Product product = productRepository.findById(dto.getProductId());
        RawMaterial rawMaterial = rawMaterialRepository.findById(dto.getRawMaterialId());

        if (product == null || rawMaterial == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Product or Raw Material not found in the database.")
                    .build();
        }

        existingRecipe.setProduct(product);
        existingRecipe.setRawMaterial(rawMaterial);
        existingRecipe.setQuantityNeeded(dto.getQuantity());

        return Response.ok(existingRecipe).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Remove an association")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = productRawMaterialRepository.deleteById(id);
        if (deleted) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
