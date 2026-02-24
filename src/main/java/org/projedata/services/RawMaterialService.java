package org.projedata.services;

import java.util.List;

import org.projedata.models.RawMaterial;
import org.projedata.repository.RawMaterialRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class RawMaterialService {
    
    @Inject
    RawMaterialRepository repository;

    public List<RawMaterial> listAll() {
        return repository.listAll();
    }

    public RawMaterial findById(Long id) {
        RawMaterial rawMaterial = repository.findById(id);
        if (rawMaterial == null) {
            throw new NotFoundException("Raw Material not found with id: " + id);
        }
        return rawMaterial;
    }

    @Transactional
    public RawMaterial create(RawMaterial rawMaterial) {
        repository.persist(rawMaterial);
        return rawMaterial;
    }

    @Transactional
    public RawMaterial update(Long id, RawMaterial updatedMaterial) {
        RawMaterial existingMaterial = this.findById(id);

        existingMaterial.setName(updatedMaterial.getName());
        existingMaterial.setStockQuantity(updatedMaterial.getStockQuantity());

        return existingMaterial;
    }

    @Transactional
    public void delete(Long id) {
        RawMaterial existingMaterial = this.findById(id);
        repository.delete(existingMaterial);
    }
}
