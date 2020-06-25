package com.renansouza.so.repository;

import com.renansouza.so.models.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OSRepository extends CrudRepository<OS, Integer> {

    List<OS> findByDefectContainingIgnoreCase(String defect);
    List<OS> findByStatus(Status status);
    List<OS> findByEmployeeName(String name);
    List<OS> findByClientName(String name);
    List<OS> findByProductDescription(String description);

}