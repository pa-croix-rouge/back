package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.exposition.dto.CreationDTO;
import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.*;

public class CreateClothProductDTO extends CreationDTO<ClothProduct> {
    private String name;
    private QuantifierDTO quantity;
    private String size;
    private String storageId;
    private int amount;
    private String gender;

    private final String limitID;

    public CreateClothProductDTO(String name, QuantifierDTO quantity, String size, String storageId, int amount, String gender, String limitID) {
        this.name = name;
        this.quantity = quantity;
        this.size = size;
        this.storageId = storageId;
        this.amount = amount;
        this.gender = gender;
        this.limitID = limitID;
    }

    @Override
    public ClothProduct toModel() {
        return new ClothProduct(null,
                new Product(null, name, quantity.toQuantifier(), null),
                ClothSize.fromLabel(size),
                ClothGender.fromLabel(gender));
    }

    public ClothProduct toModel(ProductLimit limit) {
        return new ClothProduct(null,
                new Product(null, name, quantity.toQuantifier(), limit),
                ClothSize.fromLabel(size),
                ClothGender.fromLabel(gender));
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLimitID() {
        return limitID;
    }
}
