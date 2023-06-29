package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.LocalUnit;
import fr.croixrouge.domain.repository.LocalUnitRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class LocalUnitService extends CRUDService<ID, LocalUnit, LocalUnitRepository> {

    public LocalUnitService(LocalUnitRepository localUnitRepository) {
        super(localUnitRepository);
    }

    public LocalUnit getLocalUnitByPostalCode(String postalCode) {
        return repository.findByPostalCode(postalCode).orElseThrow( () -> new NoSuchElementException("LocalUnit with postal code "+postalCode ));
    }

    public LocalUnit getLocalUnitByCode(String code) {
        return repository.findByCode(code).orElseThrow(() -> new NoSuchElementException("LocalUnit with code "+code ));
    }

    public String regenerateSecret(ID localUnitId) {
        return repository.regenerateSecret(localUnitId);
    }
}
