package fr.croixrouge.exposition.dto.product_beneficiary;

import fr.croixrouge.storage.model.BeneficiaryProduct;

import java.time.LocalDateTime;

public class BeneficiaryProductResponse {

    public Long beneficiaryId;

    public Long productId;
    public Long productLimitId;
    public Long storageId;

    public LocalDateTime date;

    public int quantity;

    public BeneficiaryProductResponse(Long beneficiaryId, Long productId, Long productLimitId, Long storageId, LocalDateTime date, int quantity) {
        this.beneficiaryId = beneficiaryId;
        this.productId = productId;
        this.productLimitId = productLimitId;
        this.storageId = storageId;
        this.date = date;
        this.quantity = quantity;
    }

    public static BeneficiaryProductResponse fromBeneficiaryProduct(BeneficiaryProduct beneficiaryProduct) {
        return new BeneficiaryProductResponse(
                beneficiaryProduct.getBeneficiary().getId().value(),
                beneficiaryProduct.product().getId().value(),
                beneficiaryProduct.product().getLimit().getId().value(),
                beneficiaryProduct.getStorage().getId().value(),
                beneficiaryProduct.date(),
                beneficiaryProduct.quantity()
        );
    }
}
