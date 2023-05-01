package fr.croixrouge.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Volunteer;
import fr.croixrouge.domain.repository.InMemoryCRUDRepository;
import fr.croixrouge.domain.repository.TimeStampIDGenerator;
import fr.croixrouge.domain.repository.VolunteerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryVolunteerRepository extends InMemoryCRUDRepository<ID, Volunteer> implements VolunteerRepository {

    public InMemoryVolunteerRepository(List<Volunteer> objects) {
        super(objects, new TimeStampIDGenerator());
    }

    public InMemoryVolunteerRepository() {
        super (new ArrayList<>(), new TimeStampIDGenerator());
    }

    @Override
    public Optional<Volunteer> findByUserId(ID id) {
        return this.objects.stream().filter(o -> o.getUser().getId().equals(id)).findFirst();
    }
}
