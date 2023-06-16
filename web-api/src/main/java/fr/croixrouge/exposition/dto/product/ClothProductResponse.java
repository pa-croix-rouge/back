package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.ClothProduct;

public class ClothProductResponse extends ProductResponse {

    private Long id;

    private String size;

    public ClothProductResponse() {
    }

    public ClothProductResponse(ClothProduct product) {
        super(product.getProduct());
        this.id = product.getId().value();
        this.size = product.getSize().getLabel();
    }

    public ClothProductResponse(Long id, Long productId, String name, QuantifierDTO quantifierDTO, String size) {
        super(productId, name, quantifierDTO);
        this.id = id;
        this.size = size;
    }

    public static ClothProductResponse fromClothProduct(ClothProduct product) {
        return new ClothProductResponse(product);
    }

    public Long getId() {
        return id;
    }

    public String getSize() {
        return size;
    }
}
