package fr.croixrouge.repository.db.product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.repository.db.product_limit.ProductLimitDB;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.product.ProductLimit;
import fr.croixrouge.storage.model.quantifier.MeasurementUnit;
import fr.croixrouge.storage.model.quantifier.Quantifier;
import fr.croixrouge.storage.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBProductRepository implements ProductRepository {

    private final ProductDBRepository productDBRepository;

    public InDBProductRepository(ProductDBRepository productDBRepository) {
        this.productDBRepository = productDBRepository;
    }

    public ProductLimit toProductLimit(ProductLimitDB productLimitDB) {
        if(productLimitDB == null)
            return null;
        return new ProductLimit(
                new ID(productLimitDB.getId()),
                productLimitDB.getDuration(),
                productLimitDB.getQuantity() == null ? null : new Quantifier(productLimitDB.getQuantity(),
                        productLimitDB.getUnit() == null ? null : MeasurementUnit.fromName( productLimitDB.getUnit()) )
        );
    }

    public ProductLimitDB toProductLimitDB(ProductLimit productLimit) {
        if(productLimit == null)
            return null;
        return new ProductLimitDB(
                productLimit.getId() == null ? null : productLimit.getId().value(),
                productLimit.getDuration(),
                productLimit.getQuantity() == null ? null : productLimit.getQuantity().getQuantity(),
                productLimit.getQuantity() == null ? null : productLimit.getQuantity().getUnit().getName()
        );
    }

    public Product toProduct(ProductDB productDB) {
        return new Product(
                new ID(productDB.getId()),
                productDB.getName(),
                new Quantifier(productDB.getQuantity(),
                        MeasurementUnit.fromName( productDB.getUnit())),
                toProductLimit(productDB.getProductLimitDB())
        );
    }

    public ProductDB toProductDB(Product product) {
        return new ProductDB(
                product.getId() == null ? null : product.getId().value(),
                product.getName(),
                product.getQuantity().getQuantity(),
                product.getQuantity().getUnit().getName(),
                toProductLimitDB(product.getLimit())
        );
    }

    @Override
    public Optional<Product> findById(ID id) {
        return productDBRepository.findById(id.value()).map(this::toProduct);
    }

    @Override
    public ID save(Product object) {
        var id = new ID(productDBRepository.save(toProductDB(object)).getId());
        object.setId(id);
        return id;
    }

    @Override
    public void delete(Product object) {
        productDBRepository.delete(toProductDB(object));
    }

    @Override
    public List<Product> findAll() {
        return StreamSupport.stream(productDBRepository.findAll().spliterator(), false)
                .map(this::toProduct)
                .toList();
    }

}
