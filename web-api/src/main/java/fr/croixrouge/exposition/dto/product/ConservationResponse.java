package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.storage.model.product.FoodConservation;

import java.util.List;

public class ConservationResponse {
    private List<String> conservations;

    public ConservationResponse() {
    }

    public ConservationResponse(List<String> conservations) {
        this.conservations = conservations;
    }

    public static ConservationResponse fromFoodConservations() {
        return new ConservationResponse(FoodConservation.getAllConservations().stream().map(FoodConservation::getLabel).toList());
    }

    public List<String> getConservations() {
        return conservations;
    }

    public void setConservations(List<String> conservations) {
        this.conservations = conservations;
    }
}
