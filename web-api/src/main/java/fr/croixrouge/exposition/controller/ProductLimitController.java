package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.exposition.dto.product.ClothProductResponse;
import fr.croixrouge.exposition.dto.product.CreateProductLimitDTO;
import fr.croixrouge.exposition.dto.product.FoodProductResponse;
import fr.croixrouge.exposition.dto.product.ProductLimitDTO;
import fr.croixrouge.service.AuthenticationService;
import fr.croixrouge.service.ProductLimitService;
import fr.croixrouge.storage.model.product.ProductLimit;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-limit")
public class ProductLimitController extends CRUDController<ID, ProductLimit, ProductLimitService, ProductLimitDTO, CreateProductLimitDTO> {

    private final AuthenticationService authenticationService;

    public ProductLimitController(ProductLimitService service, AuthenticationService authenticationService) {
        super(service);
        this.authenticationService = authenticationService;
    }

    @Override
    public ProductLimitDTO toDTO(ProductLimit model) {
        return new ProductLimitDTO(model.getId().value(),
                model.getName(),
                QuantifierDTO.fromQuantifier(model.getQuantity()),
                model.getDuration().toDays());
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable ID id, @RequestBody CreateProductLimitDTO createProductLimitDTO) {
        service.update(id, createProductLimitDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}/products")
    public ResponseEntity<Pair<List<FoodProductResponse>, List<ClothProductResponse>>> getAllProducts(HttpServletRequest request, @PathVariable ID id) {
        final ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        var pair = service.getProductsByLimit(localUnitId, id);
        var foodProducts = pair.getFirst().stream().map(FoodProductResponse::fromFoodProduct).toList();
        var clothProducts = pair.getSecond().stream().map(ClothProductResponse::fromClothProduct).toList();
        return ResponseEntity.ok(Pair.of(foodProducts, clothProducts));
    }


}
