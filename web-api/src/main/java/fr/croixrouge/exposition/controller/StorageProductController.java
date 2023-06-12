package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product.ProductListResponse;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.AuthenticationService;
import fr.croixrouge.service.StorageProductService;
import fr.croixrouge.storage.model.product.ProductList;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storage/product")
public class StorageProductController extends ErrorHandler {

    private final StorageProductService service;

    private final AuthenticationService authenticationService;

    public StorageProductController(StorageProductService service, AuthenticationService authenticationService) {
        this.service = service;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/{storageId}")
    public ResponseEntity<ProductListResponse> getProductsByStorage(@PathVariable ID storageId) {
        ProductList productList = service.getProductsByStorage(storageId);
        ProductListResponse productListResponse = ProductListResponse.fromProductList(productList);
        return ResponseEntity.ok(productListResponse);
    }

    @GetMapping(value = "/localunit")
    public ResponseEntity<ProductListResponse> getProductsByLocalUnit(HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        ProductList productList = service.getProductsByLocalUnit(localUnitId);
        ProductListResponse productListResponse = ProductListResponse.fromProductList(productList);
        return ResponseEntity.ok(productListResponse);
    }

    @GetMapping(value = "/{storageId}/{id}/quantity")
    public ResponseEntity<Integer> getProductQuantity(@PathVariable ID storageId, @PathVariable ID id) {
        return ResponseEntity.ok(service.getProductQuantity(storageId, id));
    }
}
