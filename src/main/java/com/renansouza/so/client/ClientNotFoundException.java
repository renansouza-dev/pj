package com.renansouza.so.client;

class ClientNotFoundException extends RuntimeException {

    ClientNotFoundException(String message) {
        super(message);
    }

    ClientNotFoundException(int id) {
        super("Could not find client " + id);
    }

}