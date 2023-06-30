package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.ClothProduct;

public class ClothProductResponse extends ProductResponse {

    public Long id;

    public String size;

    private String gender;

    public ClothProductResponse() {
    }

    public ClothProductResponse(ClothProduct product) {
        super(product.getProduct());
        this.id = product.getId().value();
        this.size = product.getSize().getLabel();
        this.gender = product.getGender().getLabel();
    }

    public ClothProductResponse(Long id, Long productId, String name, QuantifierDTO quantifierDTO, String size, String gender) {
        super(productId, name, quantifierDTO);
        this.id = id;
        this.size = size;
        this.gender = gender;
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

    public String getGender() {
        return gender;
    }
}
