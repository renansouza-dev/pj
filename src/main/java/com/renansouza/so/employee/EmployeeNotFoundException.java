package com.renansouza.so.employee;

class EmployeeNotFoundException extends RuntimeException {

    EmployeeNotFoundException(String message) {
        super(message);
    }

    EmployeeNotFoundException(int id) {
        super("Could not find employee " + id);
    }

}