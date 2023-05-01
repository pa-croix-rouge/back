package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.CreationDTO;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.CRUDService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

public abstract class CRUDController<K extends ID, V extends Entity<K>, S extends CRUDService<K, V, ?>, MODEL_DTO, CREATION_DTO extends CreationDTO<V>> extends ErrorHandler {

    protected final S service;

    public CRUDController(S service) {
        this.service = service;
    }

    public abstract MODEL_DTO toDTO(V model);

    public V toModel(CREATION_DTO dto) {
        return dto.toModel();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MODEL_DTO> getByID(@PathVariable K id) {
        return ResponseEntity.ok(toDTO(service.findById(id)));
    }

    @GetMapping()
    public ResponseEntity<List<MODEL_DTO>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @PostMapping()
    public ResponseEntity<K> post(@RequestBody CREATION_DTO model) {
        return ResponseEntity.ok(service.save(toModel(model)));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable K id) {
        service.delete(service.findById(id));
        return ResponseEntity.ok().build();
    }
}
