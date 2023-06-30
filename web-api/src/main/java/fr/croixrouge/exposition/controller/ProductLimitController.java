package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.exposition.dto.product.CreateProductLimitDTO;
import fr.croixrouge.exposition.dto.product.ProductLimitDTO;
import fr.croixrouge.service.ProductLimitService;
import fr.croixrouge.storage.model.product.ProductLimit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product-limit")
public class ProductLimitController extends CRUDController<ID, ProductLimit, ProductLimitService, ProductLimitDTO, CreateProductLimitDTO> {
    public ProductLimitController(ProductLimitService service) {
        super(service);
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


}
