package com.renansouza.so.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Product extends AbstractPersistable<Integer> {

    @Column(unique = true, nullable = false)
    private String description;
    private String type;
    private String brand;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "os_id")
    private OS os;

}