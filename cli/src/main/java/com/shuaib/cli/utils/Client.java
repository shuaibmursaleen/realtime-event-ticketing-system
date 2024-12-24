package com.shuaib.cli.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;

public class Client {
    private final String baseUrl;
        
    private final HttpClient httpClient;
    private final Gson gson;
        
    public Client(final String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }
        
    public <T> T get(String url, Class<T> classOfT) {
        try{
            final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + url)).build();
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return gson.fromJson(response.body(), classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T post(String url, Object body, Class<T> classOfT) {
        try{
            final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + url)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body))).build();
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), classOfT);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public <T> T patch(String url, Object body, Class<T> classOfT) {
        try {
            final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + url)).header("Content-Type", "application/json").method("PATCH", HttpRequest.BodyPublishers.ofString(gson.toJson(body))).build();
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(),classOfT);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T delete(String url, Class<T> classOfT) {
        try {
            final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + url)).DELETE().build();
            final HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), classOfT);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
