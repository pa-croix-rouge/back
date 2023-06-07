package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.model.product.Product;

import java.util.Optional;

public interface ProductRepository extends CRUDRepository<ID, Product> {

}
