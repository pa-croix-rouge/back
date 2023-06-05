package fr.croixrouge.repository.db.user_product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.repository.db.product.InDBProductRepository;
import fr.croixrouge.repository.db.storage.InDBStorageRepository;
import fr.croixrouge.repository.db.user.InDBUserRepository;
import fr.croixrouge.storage.model.UserProduct;
import fr.croixrouge.storage.repository.UserProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBUserProductRepository implements UserProductRepository {

    private final UserProductDBRepository userProductDBRepository;

    private final InDBUserRepository userRepository;

    private final InDBProductRepository productRepository;

    private final InDBStorageRepository storageRepository;

    public InDBUserProductRepository(UserProductDBRepository userProductDBRepository, InDBUserRepository userRepository, InDBProductRepository productRepository, InDBStorageRepository storageRepository) {
        this.userProductDBRepository = userProductDBRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.storageRepository = storageRepository;
    }

    public UserProduct toUserProduct(UserProductDB userProductDB) {
        return new UserProduct(
                new ID(userProductDB.getId()),
                userRepository.toUser(userProductDB.getUserDB()),
                productRepository.toProduct(userProductDB.getProductDB()),
                storageRepository.toStorage(userProductDB.getStorageDB()),
                userProductDB.getDate(),
                userProductDB.getNumber()
        );
    }


    public UserProductDB toUserProductDB(UserProduct userProduct) {
        return new UserProductDB(
                userProduct.getId().value(),
                userRepository.toUserDB(userProduct.user()),
                productRepository.toProductDB(userProduct.product()),
                storageRepository.toStorageDB(userProduct.getStorage()),
                userProduct.date(),
                userProduct.quantity()
        );
    }

    @Override
    public Optional<UserProduct> findById(ID id) {
        return userProductDBRepository.findById(id.value()).map(this::toUserProduct);
    }

    @Override
    public ID save(UserProduct object) {
        return new ID( userProductDBRepository.save(toUserProductDB(object)).getId());
    }

    @Override
    public void delete(UserProduct object) {
        userProductDBRepository.delete(toUserProductDB(object));
    }

    @Override
    public List<UserProduct> findAll() {
        return StreamSupport.stream(userProductDBRepository.findAll().spliterator(), false)
                .map(this::toUserProduct)
                .toList();
    }

    @Override
    public List<UserProduct> findAll(ID userId, ID productId) {
        return userProductDBRepository.findByUserDB_UserIDAndProductDB_Id(userId.value(), productId.value()).stream()
                .map(this::toUserProduct)
                .toList();
    }
}
