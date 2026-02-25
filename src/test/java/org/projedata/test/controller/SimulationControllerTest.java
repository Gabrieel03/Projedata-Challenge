package org.projedata.test.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SimulationControllerTest {
    
    @Test
    public void testSimulationEndpointSuccess() {
        given()
          .when()
             .get("/simulation")
          .then()
             .statusCode(200)
             .body("producedItems", notNullValue())
             .body("totalSimulationValue", notNullValue());
    }
}
