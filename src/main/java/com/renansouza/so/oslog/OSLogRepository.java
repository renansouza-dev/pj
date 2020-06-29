package com.renansouza.so.oslog;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "osLog", path = "osLog")
public interface OSLogRepository extends PagingAndSortingRepository<OSLog, Integer> {

    boolean existsOSLogById(int id);

}