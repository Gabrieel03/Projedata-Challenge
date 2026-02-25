package org.projedata.controllers;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.projedata.dto.SimulationResponseDTO;
import org.projedata.services.ProductionSimulationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/simulation")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Factory Simulation", description = "Simulates the best production scenario based on current stock")
public class SimulationController {

    @Inject
    ProductionSimulationService service;

    @GET
    @Operation(summary = "Simulate production", description = "Calculates maximum possible production prioritizing the most expensive products.")
    public SimulationResponseDTO simulate() {
        return service.simulateProduction();
    }
}
