package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.CreationDTO;
import fr.croixrouge.exposition.dto.ModelDTO;
import fr.croixrouge.service.CRUDService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

public abstract class CRUDController<K extends ID, V extends Entity<K>, S extends CRUDService<K, V, ?>, MODEL_DTO extends ModelDTO<V>, CREATION_DTO extends CreationDTO<V>> {

    private final S service;

    public CRUDController(S service) {
        this.service = service;
    }

    public abstract MODEL_DTO toDTO(V model);

    @GetMapping(value = "/{id}")
    public ResponseEntity<MODEL_DTO> getBiID(@PathVariable K id) {
        V model = service.findById(id);
        MODEL_DTO dto = toDTO(model);
        ResponseEntity<MODEL_DTO> response = ResponseEntity.ok(dto);

        return response;
    }

    @GetMapping()
    public ResponseEntity<List<MODEL_DTO>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @PostMapping()
    public void post(@RequestBody CREATION_DTO model) {
        service.save(model.toModel());
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable K id) {
        service.delete(service.findById(id));
    }

}
