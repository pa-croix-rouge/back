package fr.croix.rouge.storage.model.product;

import fr.croix.rouge.storage.model.StorageUserProduct;
import fr.croix.rouge.storage.model.qauntifier.MeasurementUnit;
import fr.croix.rouge.storage.model.qauntifier.Quantifier;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class ProductLimit {

    public static final ProductLimit NO_LIMIT = new ProductLimit(null, null);

    private final Duration duration;
    private final Quantifier quantity;

    public ProductLimit(Duration duration, Quantifier quantity) {
        this.duration = duration;
        this.quantity = quantity;
    }

    public boolean isLimitReached(List<StorageUserProduct> products) {
        if (duration == null || quantity == null) return false;

        LocalDate start = LocalDate.now().minusDays(duration.toDays());

        List<StorageUserProduct> q = products.stream().filter(p -> p.date().isAfter(start)).toList();
        if (q.isEmpty()) return false;

        MeasurementUnit unit = q.get(0).product().getQuantity().getUnit();

        Quantifier all = q.stream()
                .map(p -> p.product().getQuantity().multiply(p.quantity()))
                .reduce(new Quantifier(0, unit), Quantifier::add);
        return all.isGreaterThan(this.quantity);
    }

}
