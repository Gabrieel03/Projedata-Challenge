package org.projedata.dto;

import java.math.BigDecimal;

public class ProducedItemDTO {
    
    private String productName;
    private Integer quantityProduced;
    private BigDecimal unitPrice;
    private BigDecimal totalItemValue;
    
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantityProduced() {
        return quantityProduced;
    }

    public void setQuantityProduced(Integer quantityProduced) {
        this.quantityProduced = quantityProduced;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalItemValue() {
        return totalItemValue;
    }

    public void setTotalItemValue(BigDecimal totalItemValue) {
        this.totalItemValue = totalItemValue;
    }
}
