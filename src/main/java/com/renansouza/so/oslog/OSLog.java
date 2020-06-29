package com.renansouza.so.oslog;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.renansouza.so.employee.Employee;
import com.renansouza.so.os.OS;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class OSLog extends AbstractPersistable<Integer> {

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="os_id", nullable = false)
    private OS os;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id", nullable = false)
    private Employee employee;
    @Column(nullable = false)
    private String message;

}