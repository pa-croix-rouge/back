package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.ClothProduct;
import fr.croixrouge.storage.repository.ClothProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClothProductService extends CRUDService<ID, ClothProduct, ClothProductRepository> {

    private final StorageProductService storageProductService;

    public ClothProductService(ClothProductRepository repository, StorageProductService storageProductService) {
        super(repository);
        this.storageProductService = storageProductService;
    }

    public ClothProduct findByLocalUnitIdAndProductId(ID localUnitId, ID productID) {
        return findAllByLocalUnitId(localUnitId).stream().filter(clothProduct -> clothProduct.getId().equals(productID)).findFirst().orElseThrow();
    }

    public List<ClothProduct> findAllByLocalUnitId(ID localUnitId) {
        return storageProductService.findAllByLocalUnitId(localUnitId).stream().map(storageProduct -> this.repository.findByProductId(storageProduct.getProduct().getId())).flatMap(java.util.Optional::stream).toList();
    }
}
