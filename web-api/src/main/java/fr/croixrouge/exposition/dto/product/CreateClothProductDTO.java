package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.exposition.dto.CreationDTO;
import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.ClothProduct;

public class CreateClothProductDTO extends CreationDTO<ClothProduct> {
    private String name;
    private QuantifierDTO quantity;
    private String size;
    private String storageId;
    private int amount;

    public CreateClothProductDTO(String name, QuantifierDTO quantity, String size, String storageId, int amount) {
        this.name = name;
        this.quantity = quantity;
        this.size = size;
        this.storageId = storageId;
        this.amount = amount;
    }

    @Override
    public ClothProduct toModel() {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QuantifierDTO getQuantity() {
        return quantity;
    }

    public void setQuantity(QuantifierDTO quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
