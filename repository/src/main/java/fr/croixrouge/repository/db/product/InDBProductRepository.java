package fr.croixrouge.repository.db.product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.repository.db.localunit.LocalUnitDBRepository;
import fr.croixrouge.repository.db.product_limit.InDBProductLimitRepository;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.quantifier.MeasurementUnit;
import fr.croixrouge.storage.model.quantifier.Quantifier;
import fr.croixrouge.storage.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBProductRepository implements ProductRepository {

    private final ProductDBRepository productDBRepository;

    private final InDBProductLimitRepository inDBProductLimitRepository;

    private final LocalUnitDBRepository inDBLocalUnitRepository;

    public InDBProductRepository(ProductDBRepository productDBRepository, InDBProductLimitRepository inDBProductLimitRepository, LocalUnitDBRepository inDBLocalUnitRepository) {
        this.productDBRepository = productDBRepository;
        this.inDBProductLimitRepository = inDBProductLimitRepository;
        this.inDBLocalUnitRepository = inDBLocalUnitRepository;
    }

    public Product toProduct(ProductDB productDB) {
        return new Product(
                new ID(productDB.getId()),
                productDB.getName(),
                new Quantifier(productDB.getQuantity(),
                        MeasurementUnit.fromName(productDB.getUnit())),
                inDBProductLimitRepository.toProductLimit(productDB.getProductLimitDB()),
                new ID(productDB.getLocalUnitDB().getLocalUnitID()));
    }

    public ProductDB toProductDB(Product product) {
        return new ProductDB(
                product.getId() == null ? null : product.getId().value(),
                product.getName(),
                product.getQuantity().getQuantity(),
                product.getQuantity().getUnit().getName(),
                inDBProductLimitRepository.toProductLimitDB(product.getLimit()),
                inDBLocalUnitRepository.findById(product.getLocalUnitId().value()).orElseThrow());
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

    @Override
    public List<Product> findAllWithProductLimit(ID productLimitId) {
        return productDBRepository.findByProductLimitDB_Id(productLimitId.value()).stream()
                .map(this::toProduct)
                .toList();
    }

    @Override
    public Optional<Product> findByIdAndLocalUnitId(ID id, ID localUnitId) {
        return productDBRepository.findByIdAndLocalUnitDB_LocalUnitID(id.value(), localUnitId.value())
                .map(this::toProduct);
    }

    @Override
    public List<Product> findAllByLocalUnitId(ID localUnitId) {
        return productDBRepository.findByLocalUnitDB_LocalUnitID(localUnitId.value()).stream()
                .map(this::toProduct)
                .toList();
    }

    @Override
    public boolean isDeleted(ID id) {
        return productDBRepository.existsByIdAndDeletionDateNotNull(id.value());
    }
}
