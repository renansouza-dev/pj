package com.renansouza.so.os;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "os", path = "os")
public interface OSRepository extends PagingAndSortingRepository<OS, Integer> {

    List<OS> findByStatusIn(List<Status> statuses);

    boolean existsOSById(int id);

}