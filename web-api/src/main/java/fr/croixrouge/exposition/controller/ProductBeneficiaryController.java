package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.exposition.dto.product_beneficiary.BeneficiaryAddProductRequestDTO;
import fr.croixrouge.exposition.dto.product_beneficiary.BeneficiaryProductCounterResponse;
import fr.croixrouge.exposition.dto.product_beneficiary.BeneficiaryProductResponse;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.BeneficiaryProductService;
import fr.croixrouge.service.BeneficiaryService;
import fr.croixrouge.service.StorageProductService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping(value = "/{id}/count", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BeneficiaryProductCounterResponse> getAllProductCount(@PathVariable ID id, @RequestParam(value = "from") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate fromDate, @RequestParam(value = "to") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate toDate) {
        var pair = service.getBeneficiaryProductsCount(id, fromDate, toDate);

        return ResponseEntity.ok(new BeneficiaryProductCounterResponse(pair.getFirst(), pair.getSecond()));
    }

    @GetMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<BeneficiaryProductResponse>> getAllProduct(@PathVariable ID id, @RequestParam(value = "from") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate fromDate, @RequestParam(value = "to") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate toDate) {
        return ResponseEntity.ok(service.getBeneficiaryProducts(id, fromDate, toDate).stream().map(BeneficiaryProductResponse::fromBeneficiaryProduct).toList());
    }

    @GetMapping(value = "/{id}/quantity", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, QuantifierDTO>> getAllProductQuantity(@PathVariable ID id, @RequestParam(value = "from") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate fromDate, @RequestParam(value = "to") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate toDate) {
        Map<String, QuantifierDTO> result = service.getBeneficiaryProductsQuantity(id, fromDate, toDate).entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> QuantifierDTO.fromQuantifier(entry.getValue())
                ));
        return ResponseEntity.ok(result);
    }

}
