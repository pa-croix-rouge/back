package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product.ClothProductResponse;
import fr.croixrouge.exposition.dto.product.CreateClothProductDTO;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.*;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.ClothProduct;
import fr.croixrouge.storage.model.product.ClothSize;
import fr.croixrouge.storage.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/cloth")
public class ClothProductController extends ErrorHandler {

    private final ClothProductService service;

    private final AuthenticationService authenticationService;

    private final StorageService storageService;

    private final ProductService productService;

    private final StorageProductService storageProductService;

    public ClothProductController(ClothProductService service, AuthenticationService authenticationService, StorageService storageService, ProductService productService, StorageProductService storageProductService) {
        this.service = service;
        this.authenticationService = authenticationService;
        this.storageService = storageService;
        this.productService = productService;
        this.storageProductService = storageProductService;
    }

    public ClothProductResponse toDTO(ClothProduct model) {
        return ClothProductResponse.fromClothProduct(model);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClothProductResponse> getByID(@PathVariable ID id, HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        ClothProduct clothProduct = service.findByLocalUnitIdAndId(localUnitId, id);
        return ResponseEntity.ok(toDTO(clothProduct));
    }

    @GetMapping()
    public ResponseEntity<List<ClothProductResponse>> getAll(HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        return ResponseEntity.ok(service.findAllByLocalUnitId(localUnitId).stream().map(this::toDTO).toList());
    }

    @PostMapping()
    public ResponseEntity<ID> create(@RequestBody CreateClothProductDTO createClothProductDTO, HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        Storage storage = storageService.findByLocalUnitIdAndId(localUnitId, new ID(createClothProductDTO.getStorageId()));
        if (storage == null) {
            return ResponseEntity.notFound().build();
        }
        Product product = createClothProductDTO.toModel().getProduct();
        ID productId = productService.save(product);
        if (productId == null) {
            return ResponseEntity.badRequest().build();
        }

        Product productPersisted = productService.findById(productId);
        ClothProduct clothProduct = new ClothProduct(null, productPersisted, ClothSize.fromLabel(createClothProductDTO.getSize()));
        ID clothProductId = service.save(clothProduct);
        if (clothProductId == null) {
            return ResponseEntity.badRequest().build();
        }
        StorageProduct storageProduct = new StorageProduct(storage, productPersisted, createClothProductDTO.getAmount());
        ID storageProductId = storageProductService.save(storageProduct);
        if (storageProductId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(clothProductId);
    }
}
