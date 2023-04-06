package fr.croix.rouge.storage.repository;

import fr.croix.rouge.storage.model.product.Product;
import fr.croixrouge.domain.model.ID;

import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(ID id);

    void save(Product product);

    void delete(Product product);

}
