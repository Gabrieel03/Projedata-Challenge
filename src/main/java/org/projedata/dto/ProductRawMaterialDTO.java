package org.projedata.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ProductRawMaterialDTO {
    
    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "Raw Material ID cannot be null")
    private Long rawMaterialId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getRawMaterialId() {
        return rawMaterialId;
    }

    public void setRawMaterialId(Long rawMaterialId) {
        this.rawMaterialId = rawMaterialId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    
}
