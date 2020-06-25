package com.renansouza.so.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class OS extends AbstractPersistable<Integer> {

    @OneToOne(mappedBy = "os", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Employee employee;
    @OneToOne(mappedBy = "os", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Client client;
    @OneToOne(mappedBy = "os", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Product product;
    @Column(nullable = false)
    private String defect;
    @Enumerated(EnumType.STRING)
    private Status status = Status.OPEN;
    @OneToMany(mappedBy = "os", cascade = CascadeType.ALL)
    private List<OSLog> logs;

}