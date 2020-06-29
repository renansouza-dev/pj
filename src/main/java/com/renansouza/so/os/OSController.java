package com.renansouza.so.os;

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
@RequestMapping(value = {"/oss"})
public class OSController {

    private final OSService service;
    private final OSModelAssembler assembler;

    @GetMapping
    @ApiOperation(value = "all oss")
    CollectionModel<EntityModel<OS>> all() {
        List<EntityModel<OS>> os = service.all().stream().map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(os, linkTo(methodOn(OSController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    EntityModel<OS> one(@PathVariable int id) {
        OS os = service.one(id).orElseThrow(() -> new OSNotFoundException(id));

        return assembler.toModel(os);
    }

    @GetMapping("/responsible/{id}")
    CollectionModel<EntityModel<OS>> responsible(@PathVariable int id) {
        List<EntityModel<OS>> os = service.all().stream().map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(os, linkTo(methodOn(OSController.class).all()).withSelfRel());
    }

    @PostMapping()
    ResponseEntity<?> add(@RequestBody OS os) {
        EntityModel<OS> entityModel = assembler.toModel(service.add(os));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    ResponseEntity<EntityModel<OS>> update(@PathVariable("id") int id, @RequestBody OS os) {
        EntityModel<OS> entityModel = assembler.toModel(service.update(id, os));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<EntityModel<OS>> delete(@PathVariable("id") int id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
