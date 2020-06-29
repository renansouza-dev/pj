package com.renansouza.so.client;

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
@RequestMapping(value = {"/clients"})
public class ClientController {

    private final ClientService service;
    private final ClientModelAssembler assembler;

    @GetMapping
    @ApiOperation(value = "all clients")
    CollectionModel<EntityModel<Client>> all() {
        List<EntityModel<Client>> clients = service.all().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(clients, linkTo(methodOn(ClientController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    EntityModel<Client> one(@PathVariable int id) {
        Client client = service.one(id).orElseThrow(() -> new ClientNotFoundException(id));

        return assembler.toModel(client);
    }

    @PostMapping()
    ResponseEntity<?> add(@RequestBody Client client) {
        EntityModel<Client> entityModel = assembler.toModel(service.addClient(client));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    ResponseEntity<EntityModel<Client>> update(@PathVariable("id") int id, @RequestBody Client client) {
        EntityModel<Client> entityModel = assembler.toModel(service.updateClient(id, client));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

}
