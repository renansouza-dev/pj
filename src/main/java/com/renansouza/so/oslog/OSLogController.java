package com.renansouza.so.oslog;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
@RequestMapping(value = {"/oslogs"})
public class OSLogController {

    private final OSLogService service;
    private final OSLogModelAssembler assembler;

    @GetMapping
    @ApiOperation(value = "all os logs")
    CollectionModel<EntityModel<OSLog>> all() {
        List<EntityModel<OSLog>> oslogs = service.all().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(oslogs, linkTo(methodOn(OSLogController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    EntityModel<OSLog> one(@PathVariable int id) {
        OSLog oslog = service.one(id).orElseThrow(() -> new OSLogNotFoundException(id));

        return assembler.toModel(oslog);
    }

    @PostMapping("/{id}")
    ResponseEntity<?> add(@PathVariable("id") int id, @RequestBody OSLog oslog) {
        EntityModel<OSLog> entityModel = assembler.toModel(service.add(id, oslog));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    ResponseEntity<EntityModel<OSLog>> update(@PathVariable("id") int id, @RequestBody OSLog oslog) {
        EntityModel<OSLog> entityModel = assembler.toModel(service.update(id, oslog));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<EntityModel<OSLog>> delete(@PathVariable("id") int id) {
        service.deleteOSLog(id);

        return ResponseEntity.noContent().build();
    }

}
