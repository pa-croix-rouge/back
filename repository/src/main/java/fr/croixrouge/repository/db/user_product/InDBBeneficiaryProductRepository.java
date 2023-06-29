package fr.croixrouge.repository.db.user_product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.repository.db.beneficiary.InDBBeneficiaryRepository;
import fr.croixrouge.repository.db.product.InDBProductRepository;
import fr.croixrouge.repository.db.storage.InDBStorageRepository;
import fr.croixrouge.storage.model.BeneficiaryProduct;
import fr.croixrouge.storage.repository.BeneficiaryProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBBeneficiaryProductRepository implements BeneficiaryProductRepository {

    private final UserProductDBRepository userProductDBRepository;

    private final InDBBeneficiaryRepository beneficiaryRepository;

    private final InDBProductRepository productRepository;

    private final InDBStorageRepository storageRepository;

    public InDBBeneficiaryProductRepository(UserProductDBRepository userProductDBRepository, InDBBeneficiaryRepository beneficiaryRepository, InDBProductRepository productRepository, InDBStorageRepository storageRepository) {
        this.userProductDBRepository = userProductDBRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.productRepository = productRepository;
        this.storageRepository = storageRepository;
    }

    public BeneficiaryProduct toUserProduct(BeneficiaryProductDB beneficiaryProductDB) {
        return new BeneficiaryProduct(
                new ID(beneficiaryProductDB.getId()),
                beneficiaryRepository.toBeneficiary(beneficiaryProductDB.getBeneficiaryDB()),
                productRepository.toProduct(beneficiaryProductDB.getProductDB()),
                storageRepository.toStorage(beneficiaryProductDB.getStorageDB()),
                beneficiaryProductDB.getDate(),
                beneficiaryProductDB.getNumber()
        );
    }


    public BeneficiaryProductDB toUserProductDB(BeneficiaryProduct beneficiaryProduct) {
        return new BeneficiaryProductDB(
                beneficiaryProduct.getId() == null ? null : beneficiaryProduct.getId().value(),
                beneficiaryRepository.toBeneficiaryDB(beneficiaryProduct.getBeneficiary()),
                productRepository.toProductDB(beneficiaryProduct.product()),
                storageRepository.toStorageDB(beneficiaryProduct.getStorage()),
                beneficiaryProduct.date(),
                beneficiaryProduct.quantity()
        );
    }

    @Override
    public Optional<BeneficiaryProduct> findById(ID id) {
        return userProductDBRepository.findById(id.value()).map(this::toUserProduct);
    }

    @Override
    public ID save(BeneficiaryProduct object) {
        return new ID(userProductDBRepository.save(toUserProductDB(object)).getId());
    }

    @Override
    public void delete(BeneficiaryProduct object) {
        userProductDBRepository.delete(toUserProductDB(object));
    }

    @Override
    public List<BeneficiaryProduct> findAll() {
        return StreamSupport.stream(userProductDBRepository.findAll().spliterator(), false)
                .map(this::toUserProduct)
                .toList();
    }

    @Override
    public List<BeneficiaryProduct> findAll(ID userId, ID productId) {
        return userProductDBRepository.findByBeneficiaryDB_IdAndProductDB_Id(userId.value(), productId.value()).stream()
                .map(this::toUserProduct)
                .toList();
    }

    @Override
    public Optional<BeneficiaryProduct> findByID(ID storageId, ID productId, LocalDateTime date) {
        return userProductDBRepository.findByProductDB_IdAndStorageDB_IdAndDate(productId.value(), storageId.value(), date)
                .map(this::toUserProduct);
    }
}
