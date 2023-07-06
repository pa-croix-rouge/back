package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product.ConservationResponse;
import fr.croixrouge.exposition.dto.product.CreateProductDTO;
import fr.croixrouge.exposition.dto.product.ProductResponse;
import fr.croixrouge.exposition.dto.product.UnitResponse;
import fr.croixrouge.service.AuthenticationService;
import fr.croixrouge.service.ProductService;
import fr.croixrouge.service.StorageProductService;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.ClothGender;
import fr.croixrouge.storage.model.product.ClothSize;
import fr.croixrouge.storage.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final StorageProductService storageProductService;

    private final AuthenticationService authenticationService;

    private final ProductService service;


    public ProductController(ProductService service, StorageProductService storageProductService, AuthenticationService authenticationService) {
        this.service = service;
        this.storageProductService = storageProductService;
        this.authenticationService = authenticationService;
    }

    public ProductResponse toDTO(Product model) {
        return new ProductResponse(model);
    }

    @PostMapping()
    public ResponseEntity<ID> create(@RequestBody CreateProductDTO createProductDTO, HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        return ResponseEntity.ok(service.save(createProductDTO.toModel(localUnitId)));
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponse>> getAll(HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        return ResponseEntity.ok(service.findAll(localUnitId).stream().map(this::toDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getByID(@PathVariable ID id, HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        var clothProduct = service.findById(localUnitId, id);
        return ResponseEntity.ok(toDTO(clothProduct));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable ID id) {
        Product product = service.findById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        StorageProduct storageProduct = storageProductService.findByProduct(product);
        if (storageProduct != null) {
            storageProductService.delete(storageProduct);
        }

        service.delete(product);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/units")
    public ResponseEntity<UnitResponse> getUnits() {
        return ResponseEntity.ok(UnitResponse.fromMeasurementUnits());
    }

    @GetMapping("/conservations")
    public ResponseEntity<ConservationResponse> getConservations() {
        return ResponseEntity.ok(ConservationResponse.fromFoodConservations());
    }

    @GetMapping("/sizes")
    public ResponseEntity<List<String>> getSizes() {
        return ResponseEntity.ok(ClothSize.getAllSizes());
    }

    @GetMapping("/genders")
    public ResponseEntity<List<String>> getGenders() {
        return ResponseEntity.ok(ClothGender.getAllGenders());
    }
}
