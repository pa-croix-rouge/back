package fr.croixrouge.repository.db.storage_product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.repository.db.product.InDBProductRepository;
import fr.croixrouge.repository.db.storage.InDBStorageRepository;
import fr.croixrouge.storage.model.Storage;
import fr.croixrouge.storage.model.StorageProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.repository.StorageProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBStorageProductRepository implements StorageProductRepository {

    private final StorageProductDBRepository storageProductDBRepository;

    private final InDBProductRepository productRepository;

    private final InDBStorageRepository storageRepository;

    public InDBStorageProductRepository(StorageProductDBRepository storageProductDBRepository, InDBProductRepository productRepository, InDBStorageRepository storageRepository) {
        this.storageProductDBRepository = storageProductDBRepository;
        this.productRepository = productRepository;
        this.storageRepository = storageRepository;
    }


    public StorageProduct toStorageProduct(StorageProductDB storageProductDB) {
        return new StorageProduct(
                new ID(storageProductDB.getId()),
                storageRepository.toStorage(storageProductDB.getStorageDB()),
                productRepository.toProduct(storageProductDB.getProductDB()),
                storageProductDB.getNumber()
        );
    }

    public StorageProductDB toStorageProductDB(StorageProduct storageProduct) {
        return new StorageProductDB(
                storageProduct.getId() == null ? null : storageProduct.getId().value(),
                productRepository.toProductDB(storageProduct.getProduct()),
                storageRepository.toStorageDB(storageProduct.getStorage()),
                storageProduct.getQuantity()
        );
    }

    @Override
    public Optional<StorageProduct> findById(ID id) {
        return storageProductDBRepository.findById(id.value()).map(this::toStorageProduct);
    }

    @Override
    public ID save(StorageProduct object) {
        return new ID( storageProductDBRepository.save(toStorageProductDB(object)).getId());
    }

    @Override
    public void delete(StorageProduct object) {
        storageProductDBRepository.delete(toStorageProductDB(object));
    }

    @Override
    public List<StorageProduct> findAll() {
        return StreamSupport.stream(storageProductDBRepository.findAll().spliterator(), false)
                .map(this::toStorageProduct)
                .toList();
    }

    @Override
    public Optional<StorageProduct> findById(Storage storage, Product product) {
        return  storageProductDBRepository.findByProductDB_IdAndStorageDB_Id(storage.getId().value(), product.getId().value())
                .map(this::toStorageProduct);
    }

    @Override
    public Optional<StorageProduct> findByProduct(Product product) {
        return this.storageProductDBRepository.findByProductDB_Id(product.getId().value()).map(this::toStorageProduct);
    }

    @Override
    public List<StorageProduct> findAllByStorage(Storage storage) {
        return this.storageProductDBRepository.findAllByStorageDB_Id(storage.getId().value()).stream().map(this::toStorageProduct).toList();
    }

    @Override
    public List<StorageProduct> findAllByLocalUnit(ID localUnitId) {
        return this.storageProductDBRepository.findAllByStorageDB_LocalUnitDB_Id(localUnitId.value()).stream().map(this::toStorageProduct).toList();
    }

    @Override
    public Optional<StorageProduct> findAllByLocalUnitAndProductId(ID localUnitId, ID productId) {
        return storageProductDBRepository.findByStorageDB_LocalUnitDB_LocalUnitIDAndProductDB_Id(localUnitId.value(), productId.value()).map(this::toStorageProduct);
    }

    @Override
    public Optional<StorageProduct> findByProductId(ID productId) {
        return storageProductDBRepository.findByProductDB_Id(productId.value()).map(this::toStorageProduct);
    }
}
