package fr.croixrouge.storage.model.product;

import fr.croixrouge.storage.model.UserProduct;
import fr.croixrouge.storage.model.quantifier.MeasurementUnit;
import fr.croixrouge.storage.model.quantifier.Quantifier;

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

    public boolean isLimitReached(List<UserProduct> products) {
        if (duration == null || quantity == null) return false;

        LocalDate start = LocalDate.now().minusDays(duration.toDays());

        List<UserProduct> q = products.stream().filter(p -> p.date().isAfter(start)).toList();
        if (q.isEmpty()) return false;

        MeasurementUnit unit = q.get(0).product().getQuantity().getUnit();

        Quantifier all = q.stream()
                .map(p -> p.product().getQuantity().multiply(p.quantity()))
                .reduce(new Quantifier(0, unit), Quantifier::add);
        return all.isGreaterThan(this.quantity);
    }

}
