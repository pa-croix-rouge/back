package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.BeneficiaryProduct;
import fr.croixrouge.storage.model.product.ClothProduct;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.model.quantifier.Quantifier;
import fr.croixrouge.storage.repository.BeneficiaryProductRepository;
import fr.croixrouge.storage.repository.ClothProductRepository;
import fr.croixrouge.storage.repository.FoodProductRepository;
import fr.croixrouge.storage.service.StorageProductService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BeneficiaryProductService extends fr.croixrouge.storage.service.BeneficiaryProductService {

    private final FoodProductRepository foodProductRepository;

    private final ClothProductRepository clothProductRepository;

    public BeneficiaryProductService(BeneficiaryProductRepository beneficiaryProductRepository, StorageProductService storageProductService, FoodProductRepository foodProductRepository, ClothProductRepository clothProductRepository) {
        super(beneficiaryProductRepository, storageProductService);
        this.foodProductRepository = foodProductRepository;
        this.clothProductRepository = clothProductRepository;
    }

    public Pair<Map<FoodProduct, Integer>, Map<ClothProduct, Integer>> getBeneficiaryProductsCount(ID beneficiaryID, LocalDate from, LocalDate to) {
        var list = beneficiaryProductRepository.findAllFromToDate(beneficiaryID, from, to).stream().map(BeneficiaryProduct::product).toList();
        Map<FoodProduct, Integer> foodProducts = new HashMap<>();
        Map<ClothProduct, Integer> clothProducts = new HashMap<>();

        for (var product : list) {
            foodProductRepository.findByProductId(product.getId()).ifPresent(foodProduct -> {
                if (foodProducts.containsKey(foodProduct)) {
                    foodProducts.put(foodProduct, foodProducts.get(foodProduct) + 1);
                } else {
                    foodProducts.put(foodProduct, 1);
                }
            });
            clothProductRepository.findByProductId(product.getId()).ifPresent(clothProduct -> {
                if (clothProducts.containsKey(clothProduct)) {
                    clothProducts.put(clothProduct, clothProducts.get(clothProduct) + 1);
                } else {
                    clothProducts.put(clothProduct, 1);
                }
            });
        }

        return Pair.of(foodProducts, clothProducts);
    }

    public List<BeneficiaryProduct> getBeneficiaryProducts(ID beneficiaryID, LocalDate from, LocalDate to) {
        return beneficiaryProductRepository.findAllFromToDate(beneficiaryID, from, to);
    }

    public Map<String, Quantifier> getBeneficiaryProductsQuantity(ID beneficiaryID, LocalDate from, LocalDate to) {
        var list = beneficiaryProductRepository.findAllFromToDate(beneficiaryID, from, to);
        Map<String, Quantifier> productQuantities = new HashMap<>();

        for (var beneficiaryProduct : list) {

            if (beneficiaryProduct.quantity() <= 0) continue;

            if (productQuantities.containsKey(beneficiaryProduct.product().getName())) {
                productQuantities.put(
                        beneficiaryProduct.product().getName(),
                        productQuantities.get(beneficiaryProduct.product().getName())
                                .add(beneficiaryProduct.product().getQuantity()
                                        .multiply(beneficiaryProduct.quantity()))
                );
            } else {
                productQuantities.put(beneficiaryProduct.product().getName(), beneficiaryProduct.product().getQuantity().multiply(beneficiaryProduct.quantity()));
            }
        }

        return productQuantities.entrySet().stream().filter(entry -> entry.getValue().getQuantity() != 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
