package com.tatianaars.kanban.http;

public enum HTTPMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private final String method;

    private HTTPMethod(String method) {
        this.method = method;
    }
}