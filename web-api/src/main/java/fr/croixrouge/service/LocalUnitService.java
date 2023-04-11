package fr.croixrouge.service;

import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.repository.LocalUnitRepository;
import fr.croixrouge.exception.LocalUnitNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LocalUnitService {

    private final LocalUnitRepository localUnitRepository;

    public LocalUnitService(LocalUnitRepository localUnitRepository) {
        this.localUnitRepository = localUnitRepository;
    }

    public LocalUnit getLocalUnitByPostalCode(String postalCode) {
        LocalUnit localUnit = localUnitRepository.findByPostalCode(postalCode)
                .orElseThrow(() -> new LocalUnitNotFoundException("Local unit not found for postal code: " + postalCode));
        return localUnit;
    }
}
