package com.renansouza.so.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class OSLog extends AbstractPersistable<Integer> {

    @ManyToOne
    @JoinColumn(name="os_id", nullable = false)
    private OS os;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id", nullable = false)
    private Employee employee;
    @Column(nullable = false)
    private String message;
    @Column(name = "start", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime start;
    @Column(name = "end", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime end;

}