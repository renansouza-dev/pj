package com.renansouza.so.os;

import com.renansouza.so.client.ClientService;
import com.renansouza.so.employee.EmployeeService;
import com.renansouza.so.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class OSService {

    private final OSRepository repository;
    private final ClientService clientService;
    private final ProductService productService;
    private final EmployeeService employeeService;


    List<OS> all() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    List<OS> allByResponsible(int id) {
        final ArrayList<Status> statuses = new ArrayList<>();
        statuses.add(Status.OPEN);
        statuses.add(Status.IN_PROGESS);
        statuses.add(Status.IMPEDED);

        List<OS> pending = repository.findByStatusIn(statuses);

        return pending.stream().filter(o -> Objects.equals(o.getTechnician().getId(), id)).collect(Collectors.toList());
    }

    public Optional<OS> one(int id) {
        if (id <= 0) {
            throw new OSNotFoundException("Cannot search for an os with id '" + id + "'.");
        }

        return repository.findById(id);
    }

    OS add(final OS os) {
        if (Objects.isNull(os) || Objects.isNull(os.getReceptionist()) || Objects.isNull(os.getClient()) || Objects.isNull(os.getProduct())) {
            throw new IllegalArgumentException("Cannot insert an os with null data.");
        }

        if (os.getId() != null) {
            throw new IllegalArgumentException("OS already added.");
        }

        clientService.oneByName(os.getClient().getName()).ifPresent(os::setClient);
        productService.oneByName(os.getProduct().getDescription()).ifPresent(os::setProduct);
        employeeService.oneByName(os.getReceptionist().getName()).ifPresent(os::setReceptionist);

        if (Objects.nonNull(os.getTechnician())) {
            employeeService.oneByName(os.getTechnician().getName()).ifPresent(os::setTechnician);
        }

        return repository.save(os);
    }

    OS update(final int id, final OS os) {
        final OS osToUpdate = get(id);

        if (Objects.nonNull(os.getTechnician())) {
            employeeService.oneByName(os.getTechnician().getName()).ifPresent(osToUpdate::setTechnician);
        }
        if (Objects.nonNull(os.getReceptionist()) && os.getReceptionist() != osToUpdate.getReceptionist()) {
            employeeService.oneByName(os.getReceptionist().getName()).ifPresent(osToUpdate::setReceptionist);
        }
        if (Objects.nonNull(os.getProduct()) && os.getProduct() != osToUpdate.getProduct()) {
            productService.oneByName(os.getProduct().getDescription()).ifPresent(osToUpdate::setProduct);
        }
        if (Objects.nonNull(os.getClient()) && os.getClient() != osToUpdate.getClient()) {
            clientService.oneByName(os.getClient().getName()).ifPresent(osToUpdate::setClient);
        }
        if (Objects.nonNull(os.getStatus()) && os.getStatus() != osToUpdate.getStatus()) {
            osToUpdate.setStatus(os.getStatus());
        }
        if (Objects.nonNull(os.getDefect()) && !os.getDefect().equals(osToUpdate.getDefect())) {
            osToUpdate.setDefect(os.getDefect());
        }

        return repository.save(osToUpdate);
    }

    OS delete(final int id) {
        final OS osToUpdate = get(id);

        osToUpdate.setStatus(Status.CANCELED);
        return repository.save(osToUpdate);
    }

    private OS get(int id) {
        if (!repository.existsOSById(id)) {
            throw new OSNotFoundException(id);
        }

        final Optional<OS> osToUpdate = repository.findById(id);
        if (!osToUpdate.isPresent()) {
            throw new OSNotFoundException(id);
        }
        return osToUpdate.get();

    }

}
