package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.exposition.dto.CreationDTO;
import fr.croixrouge.model.Event;

public class EventCreationRequest extends CreationDTO<Event> {
    @Override
    public Event toModel() {
        return null;
    }
}
