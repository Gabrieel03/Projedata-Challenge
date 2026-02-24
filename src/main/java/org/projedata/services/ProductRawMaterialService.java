package org.projedata.services;

import java.util.List;
import org.projedata.dto.ProductRawMaterialDTO;
import org.projedata.models.Product;
import org.projedata.models.ProductRawMaterial;
import org.projedata.models.RawMaterial;
import org.projedata.repository.ProductRawMaterialRepository;
import org.projedata.repository.ProductRepository;
import org.projedata.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ProductRawMaterialService {
    
    @Inject
    ProductRawMaterialRepository recipeRepository;

    @Inject
    ProductRepository productRepository;

    @Inject
    RawMaterialRepository rawMaterialRepository;


    public ProductRawMaterial findById(Long id) {
        ProductRawMaterial recipe = recipeRepository.findById(id);
        if (recipe == null) {
            throw new NotFoundException("Recipe not found in the database.");
        }
        return recipe;
    }


    public List<ProductRawMaterial> listAll() {
        return recipeRepository.listAll();
    }

    public List<ProductRawMaterial> listByProductId(Long productId) {
        return recipeRepository.find("product.id", productId).list();
    }

    @Transactional
    public ProductRawMaterial create(ProductRawMaterialDTO dto) {
        Product product = productRepository.findById(dto.getProductId());
        RawMaterial rawMaterial = rawMaterialRepository.findById(dto.getRawMaterialId());

        if (product == null || rawMaterial == null) {
            throw new NotFoundException("Product or Raw Material not found in the database.");
        }

        ProductRawMaterial recipe = new ProductRawMaterial();
        recipe.setProduct(product);
        recipe.setRawMaterial(rawMaterial);
        recipe.setQuantityNeeded(dto.getQuantity());

        recipeRepository.persist(recipe);
        return recipe;
    }

    @Transactional
    public ProductRawMaterial update(Long id, ProductRawMaterialDTO dto) {
        ProductRawMaterial existingRecipe = recipeRepository.findById(id);
        if (existingRecipe == null) {
            throw new NotFoundException("Recipe not found.");
        }

        Product product = productRepository.findById(dto.getProductId());
        RawMaterial rawMaterial = rawMaterialRepository.findById(dto.getRawMaterialId());

        if (product == null || rawMaterial == null) {
            throw new NotFoundException("Product or Raw Material not found.");
        }

        existingRecipe.setProduct(product);
        existingRecipe.setRawMaterial(rawMaterial);
        existingRecipe.setQuantityNeeded(dto.getQuantity());

        return existingRecipe;
    }

    @Transactional
    public void delete(Long id) {
        boolean deleted = recipeRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Recipe not found to delete.");
        }
    }

}
