package org.projedata.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.projedata.dto.ProducedItemDTO;
import org.projedata.dto.SimulationResponseDTO;
import org.projedata.models.Product;
import org.projedata.models.ProductRawMaterial;
import org.projedata.models.RawMaterial;
import org.projedata.repository.ProductRawMaterialRepository;
import org.projedata.repository.ProductRepository;
import org.projedata.repository.RawMaterialRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductionSimulationService {

    @Inject
    ProductRepository productRepository;

    @Inject
    RawMaterialRepository rawMaterialRepository;

    @Inject
    ProductRawMaterialRepository recipeRepository;

    public SimulationResponseDTO simulateProduction() {
        SimulationResponseDTO response = new SimulationResponseDTO();

        List<RawMaterial> allMaterials = rawMaterialRepository.listAll();

        Map<Long, Integer> virtualStock = new HashMap<>();

        for (RawMaterial rm : allMaterials) {
            virtualStock.put(rm.getId(), rm.getStockQuantity());
        }

        List<Product> productsByValueDesc = productRepository.findAll(Sort.descending("price")).list();

        for (Product product : productsByValueDesc) {

            List<ProductRawMaterial> recipe = recipeRepository.find("product.id", product.getId()).list();

            if (recipe.isEmpty()) {
                continue;
            }

            int maxPossibleToProduce = Integer.MAX_VALUE;

            for (ProductRawMaterial item : recipe) {
                Long rawMaterialId = item.getRawMaterial().getId();
                Integer quantityNeeded = item.getQuantityNeeded();

                Integer stockAvailable = virtualStock.getOrDefault(rawMaterialId, 0);

                int possibleWithThisMaterial = stockAvailable / quantityNeeded;

                if (possibleWithThisMaterial < maxPossibleToProduce) {
                    maxPossibleToProduce = possibleWithThisMaterial;
                }
            }

            if (maxPossibleToProduce > 0) {

                for (ProductRawMaterial item : recipe) {
                    Long rawMaterialId = item.getRawMaterial().getId();
                    Integer quantityNeeded = item.getQuantityNeeded();

                    Integer currentStock = virtualStock.get(rawMaterialId);
                    Integer stockUsed = quantityNeeded * maxPossibleToProduce;

                    virtualStock.put(rawMaterialId, currentStock - stockUsed);
                }

                ProducedItemDTO producedItem = new ProducedItemDTO();
                producedItem.setProductName(product.getName());
                producedItem.setQuantityProduced(maxPossibleToProduce);
                producedItem.setUnitPrice(product.getPrice());

                BigDecimal totalItemValue = product.getPrice().multiply(new BigDecimal(maxPossibleToProduce));
                producedItem.setTotalItemValue(totalItemValue);

                response.getProducedItems().add(producedItem);

                BigDecimal currentTotal = response.getTotalSimulationValue();
                response.setTotalSimulationValue(currentTotal.add(totalItemValue));
            }
        }
        return response;
    }
}
