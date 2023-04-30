package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.service.StorageProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("storage/{storageId}/product")
public class StorageProductController {

    private final StorageProductService service;

    public StorageProductController(StorageProductService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}/quantity")
    public ResponseEntity<Integer> getProductQuantity(@PathVariable ID storageId, @PathVariable ID id) {
        return ResponseEntity.ok(service.getProductQuantity(storageId, id));
    }
}
