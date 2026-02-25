package org.projedata.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.projedata.dto.SimulationResponseDTO;
import org.projedata.models.Product;
import org.projedata.models.ProductRawMaterial;
import org.projedata.models.RawMaterial;
import org.projedata.repository.ProductRawMaterialRepository;
import org.projedata.repository.ProductRepository;
import org.projedata.repository.RawMaterialRepository;
import org.projedata.services.ProductionSimulationService;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@SuppressWarnings("unchecked")
public class ProductionSimulationServiceTest {

    ProductionSimulationService service;

    @InjectMock
    ProductRepository productRepository;

    @InjectMock
    RawMaterialRepository rawMaterialRepository;

    @InjectMock
    ProductRawMaterialRepository recipeRepository;

    public void testSimulateProductionSuccess() {
        RawMaterial madeira = new RawMaterial();
        madeira.setId(1L);
        madeira.setName("Madeira");
        madeira.setStockQuantity(10);

        List<RawMaterial> mockMaterials = new ArrayList<>();
        mockMaterials.add(madeira);

        Product mesa = new Product();
        mesa.setId(1L);
        mesa.setName("Mesa");
        mesa.setPrice(new BigDecimal("100.00"));

        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(mesa);

        ProductRawMaterial recipe = new ProductRawMaterial();
        recipe.setId(1L);
        recipe.setProduct(mesa);
        recipe.setRawMaterial(madeira);
        recipe.setQuantityNeeded(4);

        List<ProductRawMaterial> mockRecipes = new ArrayList<>();
        mockRecipes.add(recipe);

        // Configurar os mocks
        Mockito.when(rawMaterialRepository.listAll()).thenReturn(mockMaterials);

        PanacheQuery<Product> queryMock = Mockito.mock(PanacheQuery.class);
        Mockito.when(productRepository.findAll(Mockito.any(Sort.class))).thenReturn(queryMock);
        Mockito.when(queryMock.list()).thenReturn(mockProducts);

        PanacheQuery<ProductRawMaterial> recipeQueryMock = Mockito.mock(PanacheQuery.class);
        Mockito.when(recipeRepository.find("product.id", 1L)).thenReturn(recipeQueryMock);
        Mockito.when(recipeQueryMock.list()).thenReturn(mockRecipes);

        SimulationResponseDTO response = service.simulateProduction();

        Assertions.assertEquals(1, response.getProducedItems().size(), "Deveria ter fabricado 1 tipo de produto");
        Assertions.assertEquals(2, response.getProducedItems().get(0).getQuantityProduced(), "A matemática do gargalo falhou!");
        Assertions.assertEquals(new BigDecimal("200.00"), response.getTotalSimulationValue(), "O cálculo do valor total falhou!");
    }
}
