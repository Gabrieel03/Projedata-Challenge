package org.projedata.services;

import java.util.List;

import org.projedata.models.Product;
import org.projedata.repository.ProductRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ProductService {
    
    @Inject
    ProductRepository repository;

    public List<Product> listAll() {
        return repository.listAll();
    }

    public Product findById(Long id) {
        Product product = repository.findById(id);
        if (product == null) {
            throw new NotFoundException("Product not found with id: " + id);
        }
        return product;
    }

    @Transactional
    public Product create(Product product) {
        repository.persist(product);
        return product;
    }

    @Transactional
    public Product update(Long id, Product updatedProduct) {
        Product existingProduct = this.findById(id); 

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());

        return existingProduct;
    }

    @Transactional
    public void delete(Long id) {
        Product existingProduct = this.findById(id); 
        repository.delete(existingProduct);
    }
}
