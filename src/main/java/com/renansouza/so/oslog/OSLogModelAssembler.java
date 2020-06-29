package com.renansouza.so.oslog;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OSLogModelAssembler implements RepresentationModelAssembler<OSLog, EntityModel<OSLog>> {

    @Override
    public EntityModel<OSLog> toModel(OSLog osLog) {
        return EntityModel.of(osLog,
                linkTo(methodOn(OSLogController.class).one(osLog.getId())).withSelfRel(),
                linkTo(methodOn(OSLogController.class).all()).withRel("osLogs"));
    }

}