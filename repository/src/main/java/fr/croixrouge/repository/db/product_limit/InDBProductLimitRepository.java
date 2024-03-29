package fr.croixrouge.repository.db.product_limit;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.repository.db.localunit.LocalUnitDBRepository;
import fr.croixrouge.storage.model.product.ProductLimit;
import fr.croixrouge.storage.model.quantifier.MeasurementUnit;
import fr.croixrouge.storage.model.quantifier.Quantifier;
import fr.croixrouge.storage.repository.ProductLimitRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBProductLimitRepository implements ProductLimitRepository {

    private final ProductLimitDBRepository productLimitDBRepository;

    private final LocalUnitDBRepository inDBLocalUnitRepository;

    public InDBProductLimitRepository(ProductLimitDBRepository productLimitDBRepository, LocalUnitDBRepository inDBLocalUnitRepository) {
        this.productLimitDBRepository = productLimitDBRepository;
        this.inDBLocalUnitRepository = inDBLocalUnitRepository;
    }

    public ProductLimit toProductLimit(ProductLimitDB productLimitDB) {
        if (productLimitDB == null)
            return null;
        return new ProductLimit(
                new ID(productLimitDB.getId()),
                productLimitDB.getName(),
                productLimitDB.getDuration(),
                productLimitDB.getQuantity() == null ? null : new Quantifier(productLimitDB.getQuantity(),
                        productLimitDB.getUnit() == null ? null : MeasurementUnit.fromName(productLimitDB.getUnit())),
                new ID(productLimitDB.getLocalUnitDB().getLocalUnitID()));
    }

    public ProductLimitDB toProductLimitDB(ProductLimit productLimit) {
        if(productLimit == null)
            return null;
        return new ProductLimitDB(
                productLimit.getId() == null ? null : productLimit.getId().value(),
                productLimit.getName(),
                productLimit.getDuration(),
                productLimit.getQuantity() == null ? null : productLimit.getQuantity().getQuantity(),
                productLimit.getQuantity() == null ? null : productLimit.getQuantity().getUnit().getName(),
                inDBLocalUnitRepository.findById(productLimit.getLocalUnitId().value()).orElseThrow());
    }

    @Override
    public Optional<ProductLimit> findById(ID id) {
        return productLimitDBRepository.findById(id.value()).map(this::toProductLimit);
    }

    @Override
    public ID save(ProductLimit object) {
        var id = new ID(productLimitDBRepository.save(toProductLimitDB(object)).getId());
        object.setId(id);
        return id;
    }

    @Override
    public void delete(ProductLimit object) {
        productLimitDBRepository.delete(toProductLimitDB(object));
    }

    @Override
    public List<ProductLimit> findAll() {
        return StreamSupport.stream(productLimitDBRepository.findAll().spliterator(), false)
                .map(this::toProductLimit)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Optional<ProductLimit> findByIdAndLocalUnitId(ID id, ID localUnitId) {
        return productLimitDBRepository.findByIdAndLocalUnitDB_LocalUnitID(id.value(), localUnitId.value()).map(this::toProductLimit);
    }

    @Override
    public List<ProductLimit> findAllByLocalUnitId(ID localUnitId) {
        return productLimitDBRepository.findByLocalUnitDB_LocalUnitID(localUnitId.value()).stream()
                .map(this::toProductLimit)
                .collect(java.util.stream.Collectors.toList());
    }
}
