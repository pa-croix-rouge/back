package fr.croixrouge.repository.db.product_limit;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.ProductLimit;
import fr.croixrouge.storage.model.quantifier.MeasurementUnit;
import fr.croixrouge.storage.model.quantifier.Quantifier;
import fr.croixrouge.storage.repository.ProductLimitRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBProductLimitRepository implements ProductLimitRepository {

    private final ProductLimitDBRepository productLimitDBRepository;

    public InDBProductLimitRepository(ProductLimitDBRepository productLimitDBRepository) {
        this.productLimitDBRepository = productLimitDBRepository;
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

    @Override
    public Optional<ProductLimit> findById(ID id) {
        return productLimitDBRepository.findById(id.value()).map(this::toProductLimit);
    }

    @Override
    public ID save(ProductLimit object) {
        return new ID(productLimitDBRepository.save(toProductLimitDB(object)).getId());
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
}
