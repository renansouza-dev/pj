package com.renansouza.so.os;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OSModelAssembler implements RepresentationModelAssembler<OS, EntityModel<OS>> {

    @Override
    public EntityModel<OS> toModel(OS osLog) {
        return EntityModel.of(osLog,
                linkTo(methodOn(OSController.class).one(osLog.getId())).withSelfRel(),
                linkTo(methodOn(OSController.class).all()).withRel("oss"));
    }

}