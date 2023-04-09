package fr.croix.rouge.storage.repository;

import fr.croix.rouge.storage.model.product.Product;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;

import java.util.Optional;

public interface ProductRepository extends CRUDRepository<ID, Product> {

    Optional<Product> findById(ID id);

    void save(Product product);

    void delete(Product product);

}
