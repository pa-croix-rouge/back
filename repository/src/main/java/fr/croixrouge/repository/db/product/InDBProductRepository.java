package fr.croixrouge.repository.db.product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.repository.db.product_limit.ProductLimitDB;
import fr.croixrouge.repository.db.product_limit.ProductLimitDBRepository;
import fr.croixrouge.storage.model.product.FoodProduct;
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

    private final FoodProductDBRepository foodProductDBRepository;

    private final ProductLimitDBRepository productLimitDBRepository;

    public InDBProductRepository(ProductDBRepository productDBRepository, FoodProductDBRepository foodProductDBRepository, ProductLimitDBRepository productLimitDBRepository) {
        this.productDBRepository = productDBRepository;
        this.foodProductDBRepository = foodProductDBRepository;
        this.productLimitDBRepository = productLimitDBRepository;
    }

    public ProductLimit toProductLimit(ProductLimitDB productLimitDB) {
        if(productLimitDB == null)
            return null;
        return new ProductLimit(
                new ID(productLimitDB.getId()),
                productLimitDB.getDuration(),
                new Quantifier(productLimitDB.getQuantity(),
                        MeasurementUnit.fromName( productLimitDB.getUnit()) )
        );
    }

    public ProductLimitDB toProductLimitDB(ProductLimit productLimit) {
        if(productLimit == null)
            return null;
        return new ProductLimitDB(
                productLimit.getId() == null ? null : productLimit.getId().value(),
                productLimit.getDuration(),
                productLimit.getQuantity().getQuantity(),
                productLimit.getQuantity().getUnit().getName()
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

    public FoodProduct toFoodProduct(FoodProductDB foodProductDB) {
        var product = findById(new ID(foodProductDB.getId())).orElseThrow();

        return new FoodProduct(
                product,
                foodProductDB.getFoodConservation(),
                foodProductDB.getExpirationDate(),
                foodProductDB.getOptimalConsumptionDate(),
                foodProductDB.getPrice()
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
        return new ID( productDBRepository.save(toProductDB(object)).getId());
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
    public Optional<FoodProduct> findByIdFood(ID id) {
        return foodProductDBRepository.findById(id.value()).map(this::toFoodProduct);
    }
}
