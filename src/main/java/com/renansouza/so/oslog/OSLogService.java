package com.renansouza.so.oslog;

import com.renansouza.so.client.ClientService;
import com.renansouza.so.employee.EmployeeService;
import com.renansouza.so.os.OSService;
import com.renansouza.so.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class OSLogService {

    private final OSLogRepository repository;
    private final OSService osService;

    List<OSLog> all() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    Optional<OSLog> one(int id) {
        if (id <= 0) {
            throw new OSLogNotFoundException("Cannot search for an os log with id '" + id + "'.");
        }

        return repository.findById(id);
    }

    OSLog add(final int id, final OSLog oslog) {
        if (Objects.isNull(oslog) || id <= 0 ||
                StringUtils.isEmpty(oslog.getMessage()) || Objects.isNull(oslog.getEmployee())) {
            throw new IllegalArgumentException("Cannot insert an os log with null data.");
        }

        if (oslog.getId() != null) {
            throw new OSLogNotFoundException("os log already added.");
        }

        osService.one(id).ifPresent(oslog::setOs);
        return repository.save(oslog);
    }

    OSLog update(final int id, final OSLog oslog) {
        if (id <= 0 || oslog == null || StringUtils.isEmpty(oslog.getMessage()) || oslog.getEmployee() == null) {
            throw new IllegalArgumentException("Cannot insert an os log with null data.");
        }

        final OSLog osLogToUpdate = get(id);
        if (!StringUtils.isEmpty(oslog.getMessage())) {
            osLogToUpdate.setMessage(oslog.getMessage());
        }
        if (oslog.getEmployee() != null) {
            osLogToUpdate.setEmployee(oslog.getEmployee());
        }

        return repository.save(osLogToUpdate);
    }

    void deleteOSLog(final int id) {
        final OSLog osLogToUpdate = get(id);

        repository.deleteById(id);
    }

    private OSLog get(int id) {
        if (!repository.existsOSLogById(id)) {
            throw new OSLogNotFoundException(id);
        }

        final Optional<OSLog> oslogToUpdate = repository.findById(id);
        if (!oslogToUpdate.isPresent()) {
            throw new OSLogNotFoundException(id);
        }

        return oslogToUpdate.get();
    }

}
