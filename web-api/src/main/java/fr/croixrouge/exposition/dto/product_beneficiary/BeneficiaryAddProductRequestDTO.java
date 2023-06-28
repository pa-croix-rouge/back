package fr.croixrouge.exposition.dto.product_beneficiary;

import java.time.LocalDate;

public class BeneficiaryAddProductRequestDTO {

    public Long beneficiaryId;

    public Long storageProductId;


    public int quantity;

    public LocalDate date;

    public BeneficiaryAddProductRequestDTO() {
    }

    public BeneficiaryAddProductRequestDTO(Long beneficiaryId, Long storageProductId, int quantity, LocalDate date) {
        this.beneficiaryId = beneficiaryId;
        this.storageProductId = storageProductId;
        this.quantity = quantity;
        this.date = date;
    }
}
