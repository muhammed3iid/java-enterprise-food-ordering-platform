package com.example.foodorderingplatform.Utils;

public class Response<T> {
    private String message;
    private T object;

    public Response(String message, T object) {
        this.message = message;
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public T getObject() {
        return object;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
