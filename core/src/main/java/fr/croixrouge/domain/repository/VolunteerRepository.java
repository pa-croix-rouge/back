package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Volunteer;

import java.util.Optional;

public interface VolunteerRepository extends CRUDRepository<ID, Volunteer>{

    Optional<Volunteer> findByUserId(ID id);

    Optional<Volunteer> findByUsername(String username);

    boolean validateVolunteerAccount(Volunteer volunteer);

    boolean invalidateVolunteerAccount(Volunteer volunteer);
}
