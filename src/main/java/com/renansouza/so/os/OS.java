package com.renansouza.so.os;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.renansouza.so.client.Client;
import com.renansouza.so.employee.Employee;
import com.renansouza.so.oslog.OSLog;
import com.renansouza.so.product.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class OS extends AbstractPersistable<Integer> {

    @JsonManagedReference
    @OneToOne(mappedBy = "os", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Employee receptionist;
    @JsonManagedReference
    @OneToOne(mappedBy = "os", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Employee technician;
    @OneToOne(mappedBy = "os", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Client client;
    @OneToOne(mappedBy = "os", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Product product;
    @Column(nullable = false)
    private String defect;
    @Enumerated(EnumType.STRING)
    private Status status = Status.OPEN;
    @JsonManagedReference
    @OneToMany(mappedBy = "os", cascade = CascadeType.ALL)
    private List<OSLog> logs;

}