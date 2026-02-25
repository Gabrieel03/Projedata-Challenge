package org.projedata.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SimulationResponseDTO {
    
    private List<ProducedItemDTO> producedItems = new ArrayList<>();
    private BigDecimal totalSimulationValue = BigDecimal.ZERO;

    public List<ProducedItemDTO> getProducedItems() {
        return producedItems;
    }

    public void setProducedItems(List<ProducedItemDTO> producedItems) {
        this.producedItems = producedItems;
    }

    public BigDecimal getTotalSimulationValue() {
        return totalSimulationValue;
    }

    public void setTotalSimulationValue(BigDecimal totalSimulationValue) {
        this.totalSimulationValue = totalSimulationValue;
    }
}
