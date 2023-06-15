package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product.ClothProductResponse;
import fr.croixrouge.exposition.dto.product.CreateClothProductDTO;
import fr.croixrouge.service.ClothProductService;
import fr.croixrouge.storage.model.product.ClothProduct;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/cloth")
public class ClothProductController extends CRUDController<ID, ClothProduct, ClothProductService, ClothProductResponse, CreateClothProductDTO> {

    public ClothProductController(ClothProductService service) {
        super(service);
    }

    @Override
    public ClothProductResponse toDTO(ClothProduct model) {
        return ClothProductResponse.fromClothProduct(model);
    }
}
