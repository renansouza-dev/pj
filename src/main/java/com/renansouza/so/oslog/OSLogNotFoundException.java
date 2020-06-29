package com.renansouza.so.oslog;

class OSLogNotFoundException extends RuntimeException {

    OSLogNotFoundException(String message) {
        super(message);
    }

    OSLogNotFoundException(int id) {
        super("Could not find os log " + id);
    }

}