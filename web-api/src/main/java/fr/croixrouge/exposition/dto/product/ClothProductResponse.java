package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.storage.model.product.ClothProduct;
import fr.croixrouge.storage.model.product.ClothSize;

public class ClothProductResponse extends ProductResponse {

    private ClothSize size;

    public ClothProductResponse() {
    }

    public ClothProductResponse(ClothProduct product) {
        super(product.getProduct());
        this.size = product.getSize();
    }

    public ClothSize getSize() {
        return size;
    }
}
