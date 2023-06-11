package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.ClothProduct;
import fr.croixrouge.storage.model.product.ClothSize;

public class ClothProductResponse extends ProductResponse {

    private Long id;

    private ClothSize size;

    public ClothProductResponse() {
    }

    public ClothProductResponse(ClothProduct product) {
        super(product.getProduct());
        this.id = product.getId().value();
        this.size = product.getSize();
    }

    public ClothProductResponse(Long id, Long productId, String name, QuantifierDTO quantifierDTO, ClothSize size) {
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

    public ClothSize getSize() {
        return size;
    }
}
