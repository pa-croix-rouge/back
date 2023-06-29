package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product_beneficiary.BeneficiaryAddProductRequestDTO;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.BeneficiaryProductService;
import fr.croixrouge.service.BeneficiaryService;
import fr.croixrouge.service.StorageProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/beneficiary")
public class ProductBeneficiaryController extends ErrorHandler {

    private final BeneficiaryProductService service;

    private final BeneficiaryService beneficiaryService;

    private final StorageProductService storageProductService;

    public ProductBeneficiaryController(BeneficiaryProductService service, StorageProductService storageProductService, BeneficiaryService beneficiaryService) {
        this.service = service;
        this.storageProductService = storageProductService;
        this.beneficiaryService = beneficiaryService;
    }


    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ID> addProduct(@RequestBody BeneficiaryAddProductRequestDTO request) {
        var beneficiary = beneficiaryService.findById(new ID(request.beneficiaryId));
        var storage = storageProductService.findByID(new ID(request.storageProductId));

        return ResponseEntity.ok(service.addProduct(beneficiary, storage, request.quantity, request.date.atStartOfDay()));
    }

}
