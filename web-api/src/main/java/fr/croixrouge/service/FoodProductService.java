package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.repository.FoodProductRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class FoodProductService extends CRUDService<ID, FoodProduct, FoodProductRepository> {

    private final StorageProductService storageProductService;

    public FoodProductService(FoodProductRepository repository, StorageProductService storageProductService) {
        super(repository);
        this.storageProductService = storageProductService;
    }

    public FoodProduct findByLocalUnitIdAndId(ID localUnitId, ID productID) {
        return findAllByLocalUnitId(localUnitId).stream().filter(clothProduct -> clothProduct.getId().equals(productID)).findFirst().orElseThrow();
    }

    public List<FoodProduct> findAllByLocalUnitId(ID localUnitId) {
        return storageProductService.findAllByLocalUnitId(localUnitId).stream().map(storageProduct -> this.repository.findByProductId(storageProduct.getId())).flatMap(java.util.Optional::stream).toList();
    }

    public List<FoodProduct> findAllSoonExpiredByLocalUnitId(ID localUnitId) {
        return findAllByLocalUnitId(localUnitId).stream()
                .filter(foodProduct -> foodProduct.getExpirationDate().minusDays(7).isBefore(ZonedDateTime.now()))
                .toList();
    }
}
