package fr.croixrouge.repository.db.product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.ClothProduct;
import fr.croixrouge.storage.repository.ClothProductRepository;
import fr.croixrouge.storage.repository.StorageProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBClothProductRepository implements ClothProductRepository {
    private final ClothProductDBRepository clothProductDBRepository;
    private final InDBProductRepository inDBProductRepository;
    private final StorageProductRepository storageProductRepository;

    public InDBClothProductRepository(ClothProductDBRepository clothProductDBRepository, InDBProductRepository inDBProductRepository, StorageProductRepository storageProductRepository) {
        this.clothProductDBRepository = clothProductDBRepository;
        this.inDBProductRepository = inDBProductRepository;
        this.storageProductRepository = storageProductRepository;
    }

    public ClothProduct toClothProduct(ClothProductDB clothProductDB) {
        return new ClothProduct(
                ID.of(clothProductDB.getId()),
                inDBProductRepository.findById(new ID(clothProductDB.getProductDB().getId())).orElseThrow(),
                clothProductDB.getSize()
        );
    }

    public ClothProductDB toClothProductDB(ClothProduct clothProduct) {
        return new ClothProductDB(
                clothProduct.getId() == null ? null : clothProduct.getId().value(),
                inDBProductRepository.toProductDB(inDBProductRepository.findById(clothProduct.getProduct().getId()).orElseThrow()),
                clothProduct.getSize()
        );
    }

    @Override
    public Optional<ClothProduct> findById(ID id) {
        return clothProductDBRepository.findById(id.value()).map(this::toClothProduct);
    }

    @Override
    public Optional<ClothProduct> findByLocalUnitIdAndId(ID localUnitId, ID id) {
        ClothProduct clothProduct = clothProductDBRepository.findById(id.value()).map(this::toClothProduct).orElse(null);
        if (clothProduct == null) {
            return Optional.empty();
        }
        StorageProduct storageProduct = storageProductRepository.findByProduct(clothProduct.getProduct()).orElse(null);
        if (storageProduct == null) {
            return Optional.empty();
        }
        if (storageProduct.getStorage().getLocalUnit().getId().equals(localUnitId)) {
            return Optional.of(clothProduct);
        }
        return Optional.empty();
    }

    @Override
    public ID save(ClothProduct object) {
        return new ID( clothProductDBRepository.save(toClothProductDB(object)).getId() );
    }

    @Override
    public void delete(ClothProduct object) {
        clothProductDBRepository.delete(toClothProductDB(object));
    }

    @Override
    public List<ClothProduct> findAll() {
        return StreamSupport.stream(clothProductDBRepository.findAll().spliterator(), false)
                .map(this::toClothProduct)
                .toList();
    }

    @Override
    public List<ClothProduct> findAllByLocalUnitId(ID localUnitId) {
        List<ClothProduct> clothProducts = StreamSupport.stream(clothProductDBRepository.findAll().spliterator(), false)
                .map(this::toClothProduct)
                .toList();

        List<StorageProduct> storageProducts = clothProducts.stream().map(ClothProduct::getProduct)
                .map(storageProductRepository::findByProduct)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(storageProduct -> storageProduct.getStorage().getLocalUnit().getId().equals(localUnitId))
                .toList();

        List<ClothProduct> localClothProducts = new ArrayList<>();
        for (ClothProduct clothProduct : clothProducts) {
            for (StorageProduct storageProduct : storageProducts) {
                if (clothProduct.getProduct().getId().equals(storageProduct.getProduct().getId())) {
                    localClothProducts.add(clothProduct);
                }
            }
        }
        return localClothProducts;
    }

    @Override
    public Optional<ClothProduct> findByProductId(ID productId) {
        return clothProductDBRepository.findByProductId(productId.value()).map(this::toClothProduct);
    }
}
