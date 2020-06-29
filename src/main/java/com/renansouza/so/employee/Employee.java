package com.renansouza.so.employee;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.renansouza.so.os.OS;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Employee extends AbstractPersistable<Integer> {

    @Column(unique = true, nullable = false)
    private String name;

    private boolean active = true;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "os_id")
    private OS os;

}