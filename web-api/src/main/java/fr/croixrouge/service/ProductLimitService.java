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

@Service
public class ProductLimitService extends CRUDService<ID, ProductLimit, ProductLimitRepository> {

    private final ClothProductService clothProductService;

    private final FoodProductService foodProductService;

    public ProductLimitService(ProductLimitRepository productLimitRepository, ClothProductService clothProductService, FoodProductService foodProductService) {
        super(productLimitRepository);
        this.clothProductService = clothProductService;
        this.foodProductService = foodProductService;
    }

    public void update(ID id, CreateProductLimitDTO createProductLimitDTO) {

        var productLimit = findById(id);

        var newProductLimit = new ProductLimit(id,
                createProductLimitDTO.getName() == null ? productLimit.getName() : createProductLimitDTO.getName(),
                createProductLimitDTO.getDuration() == null ? productLimit.getDuration() : Duration.ofDays(createProductLimitDTO.getDuration()),
                createProductLimitDTO.getQuantity() == null ? productLimit.getQuantity() : createProductLimitDTO.getQuantity().toQuantifier()
        );

        save(newProductLimit);
    }

    public Pair<List<FoodProduct>, List<ClothProduct>> getProductsByLimit(ID localUnitId, ID limitId) {
        return Pair.of(foodProductService.findAllByLocalUnitIdAndProductLimitId(localUnitId, limitId), clothProductService.findAllByLocalUnitIdAndProductLimitId(localUnitId, limitId));
    }
}
