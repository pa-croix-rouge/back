package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product_beneficiary.BeneficiaryAddProductRequestDTO;
import fr.croixrouge.exposition.dto.product_beneficiary.BeneficiaryProductCounterResponse;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.BeneficiaryProductService;
import fr.croixrouge.service.BeneficiaryService;
import fr.croixrouge.service.StorageProductService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BeneficiaryProductCounterResponse> getAllProduct(@PathVariable ID id, @RequestParam(value = "from") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate fromDate, @RequestParam(value = "to") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate toDate) {
        var pair = service.getBeneficiaryProducts(id, fromDate, toDate);

        return ResponseEntity.ok(new BeneficiaryProductCounterResponse(pair.getFirst(), pair.getSecond()));
    }


}
