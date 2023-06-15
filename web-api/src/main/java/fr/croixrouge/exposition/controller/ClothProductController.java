package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product.ClothProductResponse;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.AuthenticationService;
import fr.croixrouge.service.ClothProductService;
import fr.croixrouge.storage.model.product.ClothProduct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product/cloth")
public class ClothProductController extends ErrorHandler {

    private final ClothProductService service;

    private final AuthenticationService authenticationService;

    public ClothProductController(ClothProductService service, AuthenticationService authenticationService) {
        this.service = service;
        this.authenticationService = authenticationService;
    }

    public ClothProductResponse toDTO(ClothProduct model) {
        return ClothProductResponse.fromClothProduct(model);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClothProductResponse> getByID(@PathVariable ID id, HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        return ResponseEntity.ok(toDTO(service.findByLocalUnitIdAndId(localUnitId, id)));
    }

    @GetMapping()
    public ResponseEntity<List<ClothProductResponse>> getAll(HttpServletRequest request) {
        ID localUnitId = authenticationService.getUserLocalUnitIdFromJwtToken(request);
        return ResponseEntity.ok(service.findAllByLocalUnitId(localUnitId).stream().map(this::toDTO).toList());
    }
}
