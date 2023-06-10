package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product.StorageProductResponse;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.StorageProductService;
import fr.croixrouge.storage.model.product.ProductList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/storage/product")
public class StorageProductController extends ErrorHandler {

    private final StorageProductService service;

    public StorageProductController(StorageProductService service) {
        this.service = service;
    }

    @GetMapping("/{storageId}")
    public ResponseEntity<List<StorageProductResponse>> getProductsByStorage(@PathVariable ID storageId) {
        return ResponseEntity.ok(service.getProductsByStorage(storageId).stream().map(StorageProductResponse::fromStorageProduct).toList());
    }

    @GetMapping(value = "/localunit/{id}")
    public ResponseEntity<List<StorageProductResponse>> getProductsByLocalUnit(@PathVariable ID id) {
        ProductList productList = service.getProductsByLocalUnit(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{storageId}/{id}/quantity")
    public ResponseEntity<Integer> getProductQuantity(@PathVariable ID storageId, @PathVariable ID id) {
        return ResponseEntity.ok(service.getProductQuantity(storageId, id));
    }
}
