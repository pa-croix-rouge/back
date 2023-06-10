package fr.croixrouge.repository.db.product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.ClothProduct;
import fr.croixrouge.storage.repository.ClothProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBClothProductRepository implements ClothProductRepository {
    private final ClothProductDBRepository clothProductDBRepository;
    private final InDBProductRepository inDBProductRepository;

    public InDBClothProductRepository(ClothProductDBRepository clothProductDBRepository, InDBProductRepository inDBProductRepository) {
        this.clothProductDBRepository = clothProductDBRepository;
        this.inDBProductRepository = inDBProductRepository;
    }

    public ClothProduct toClothProduct(ClothProductDB clothProductDB) {
        return new ClothProduct(
                inDBProductRepository.findById(new ID(clothProductDB.getId())).orElseThrow(),
                clothProductDB.getSize()
        );
    }

    public ClothProductDB toClothProductDB(ClothProduct clothProduct) {
        return new ClothProductDB(
                inDBProductRepository.toProductDB(inDBProductRepository.findById(clothProduct.getId()).orElseThrow()),
                clothProduct.getSize()
        );
    }

    @Override
    public Optional<ClothProduct> findById(ID id) {
        return clothProductDBRepository.findById(id.value()).map(this::toClothProduct);
    }

    @Override
    public ID save(ClothProduct object) {
        var id = inDBProductRepository.save(object);
        object.setId(id);
        return new ID( clothProductDBRepository.save(toClothProductDB(object)).getId() );
    }

    @Override
    public void delete(ClothProduct object) {
        clothProductDBRepository.delete(toClothProductDB(object));
        inDBProductRepository.delete(object);
    }

    @Override
    public List<ClothProduct> findAll() {
        return StreamSupport.stream(clothProductDBRepository.findAll().spliterator(), false)
                .map(this::toClothProduct)
                .toList();
    }
}
