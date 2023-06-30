package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.ProductLimit;

public class ProductLimitDTO {

    public Long id;

    public String name;

    public QuantifierDTO quantity;

    public Long duration;

    public ProductLimitDTO(Long id, String name, QuantifierDTO quantity, Long duration) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.duration = duration;
    }

    public ProductLimitDTO() {
    }

    public static ProductLimitDTO of(ProductLimit limit) {
        if (limit == null) {
            return null;
        }
        return new ProductLimitDTO(limit.getId().value(), limit.getName(), new QuantifierDTO(limit.getQuantity()), limit.getDuration().toDays());
    }

    public Long getId() {
        return id;
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
}
