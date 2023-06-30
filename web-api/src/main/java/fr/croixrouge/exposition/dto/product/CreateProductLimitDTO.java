package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.exposition.dto.CreationDTO;
import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.ProductLimit;

import java.time.Duration;

public class CreateProductLimitDTO extends CreationDTO<ProductLimit> {

    public String name;

    public QuantifierDTO quantity;

    public Long duration;

    public CreateProductLimitDTO(String name, QuantifierDTO quantity, Long duration) {
        this.name = name;
        this.quantity = quantity;
        this.duration = duration;
    }

    public CreateProductLimitDTO() {
    }

    public String getName() {
        return name;
    }

    public QuantifierDTO getQuantity() {
        return quantity;
    }

    public Long getDuration() {
        return duration;
    }

    @Override
    public ProductLimit toModel() {
        return new ProductLimit(null, name, Duration.ofDays(duration), quantity.toQuantifier());
    }
}
