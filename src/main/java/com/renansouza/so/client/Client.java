package com.renansouza.so.client;

import com.renansouza.so.os.OS;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Client extends AbstractPersistable<Integer> {

    @Column(unique = true, nullable = false)
    private String name;
    private String address;
    private int zipcode;
    private String phone;
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "os_id")
    private OS os;

}