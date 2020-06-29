package com.renansouza.so.os;

class OSNotFoundException extends RuntimeException {

    OSNotFoundException(String message) {
        super(message);
    }

    OSNotFoundException(int id) {
        super("Could not find os " + id);
    }

}