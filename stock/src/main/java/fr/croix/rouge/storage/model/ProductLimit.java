package fr.croix.rouge.storage.model;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class ProductLimit {

    private final Duration duration;
    private final Quantifier quantity;

    public ProductLimit(Duration duration, Quantifier quantity) {
        this.duration = duration;
        this.quantity = quantity;
    }

    public boolean isLimitReached(List<StorageUserProduct> products) {
        LocalDate start = LocalDate.now().minusDays(duration.toDays());

        List<Quantifier> q = products.stream().filter(p -> p.date().isAfter(start)).map(sup -> sup.product().Quantity).toList();
        if (q.isEmpty()) return false;

        Quantifier all = q.stream().reduce(new Quantifier(0, q.get(0).getUnit().getBaseUnit()), Quantifier::add);
        return all.isGreaterOrEqualThan(this.quantity);
    }

}
