package com.renansouza.so.product;

class ProductNotFoundException extends RuntimeException {

    ProductNotFoundException(String message) {
        super(message);
    }

    ProductNotFoundException(int id) {
        super("Could not find product " + id);
    }

}