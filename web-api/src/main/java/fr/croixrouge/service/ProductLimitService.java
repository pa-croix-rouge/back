package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.product.CreateProductLimitDTO;
import fr.croixrouge.storage.model.product.ClothProduct;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.model.product.ProductLimit;
import fr.croixrouge.storage.repository.ProductLimitRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductLimitService extends CRUDService<ID, ProductLimit, ProductLimitRepository> {

    private final ClothProductService clothProductService;

    private final FoodProductService foodProductService;

    private final ProductService productService;

    public ProductLimitService(ProductLimitRepository productLimitRepository, ClothProductService clothProductService, FoodProductService foodProductService, ProductService productService) {
        super(productLimitRepository);
        this.clothProductService = clothProductService;
        this.foodProductService = foodProductService;
        this.productService = productService;
    }

    public ProductLimit findById(ID localUnitId, ID id) {
        return repository.findByIdAndLocalUnitId(id, localUnitId)
                .orElseThrow(NoSuchElementException::new);
    }

    public List<ProductLimit> findAll(ID localUnitId) {
        return repository.findAllByLocalUnitId(localUnitId);
    }

    public void update(ID id, CreateProductLimitDTO createProductLimitDTO) {

        var productLimit = findById(id);

        var newProductLimit = new ProductLimit(id,
                createProductLimitDTO.getName() == null ? productLimit.getName() : createProductLimitDTO.getName(),
                createProductLimitDTO.getDuration() == null ? productLimit.getDuration() : Duration.ofDays(createProductLimitDTO.getDuration()),
                createProductLimitDTO.getQuantity() == null ? productLimit.getQuantity() : createProductLimitDTO.getQuantity().toQuantifier(),
                productLimit.getLocalUnitId());

        save(newProductLimit);
    }

    @Override
    public void delete(ProductLimit object) {
        productService.removeAllProductLimit(object.getId());
        super.delete(object);
    }

    public Pair<List<FoodProduct>, List<ClothProduct>> getProductsByLimit(ID localUnitId, ID limitId) {
        return Pair.of(foodProductService.findAllByLocalUnitIdAndProductLimitId(localUnitId, limitId), clothProductService.findAllByLocalUnitIdAndProductLimitId(localUnitId, limitId));
    }
}
