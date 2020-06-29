package com.renansouza.so.product;

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
@RequestMapping(value = {"/products"})
public class ProductController {

    private final ProductService service;
    private final ProductModelAssembler assembler;

    @GetMapping
    @ApiOperation(value = "all products")
    CollectionModel<EntityModel<Product>> all() {
        List<EntityModel<Product>> products = service.all().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(products, linkTo(methodOn(ProductController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    EntityModel<Product> one(@PathVariable int id) {
        Product product = service.one(id).orElseThrow(() -> new ProductNotFoundException(id));

        return assembler.toModel(product);
    }

    @PostMapping()
    ResponseEntity<?> add(@RequestBody Product product) {
        EntityModel<Product> entityModel = assembler.toModel(service.addProduct(product));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    ResponseEntity<EntityModel<Product>> update(@PathVariable("id") int id, @RequestBody Product product) {
        EntityModel<Product> entityModel = assembler.toModel(service.updateProduct(id, product));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

}
