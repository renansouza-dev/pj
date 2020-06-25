package com.renansouza.so.repository;

import com.renansouza.so.models.OSLog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OSLogRepository extends CrudRepository<OSLog, Integer> {

    List<OSLog> findByMessageContainingIgnoreCase(String message);

}