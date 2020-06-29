package com.renansouza.so.employee;

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
@RequestMapping(value = {"/employees"})
public class EmployeeController {

    private final EmployeeService service;
    private final EmployeeModelAssembler assembler;

    @GetMapping
    @ApiOperation(value = "all employees")
    CollectionModel<EntityModel<Employee>> all() {
        List<EntityModel<Employee>> employees = service.all().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    EntityModel<Employee> one(@PathVariable int id) {
        Employee employee = service.one(id).orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
    }

    @PostMapping()
    ResponseEntity<?> add(@RequestBody Employee employee) {
        EntityModel<Employee> entityModel = assembler.toModel(service.add(employee));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    ResponseEntity<EntityModel<Employee>> update(@PathVariable("id") int id, @RequestBody Employee employee) {
        EntityModel<Employee> entityModel = assembler.toModel(service.update(id, employee));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<EntityModel<Employee>> delete(@PathVariable("id") int id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
