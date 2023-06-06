package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product.CreateFoodProductDTO;
import fr.croixrouge.exposition.dto.product.FoodProductResponse;
import fr.croixrouge.service.FoodProductService;
import fr.croixrouge.storage.model.product.FoodProduct;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/food")
public class FoodProductController extends CRUDController<ID, FoodProduct, FoodProductService, FoodProductResponse, CreateFoodProductDTO> {

    public FoodProductController(FoodProductService service) {
        super(service);
    }

    @Override
    public FoodProductResponse toDTO(FoodProduct model) {
        return new FoodProductResponse(model);
    }
}
