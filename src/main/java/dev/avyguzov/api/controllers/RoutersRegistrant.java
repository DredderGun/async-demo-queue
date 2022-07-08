package dev.avyguzov.api.controllers;

import spark.Route;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Register new endpoints in Spark.
 */
public class RoutersRegistrant {

    /**
     * Add a POST handler
     */
    public void addPostRoute(String path, String acceptType, Route router) {
        post(path, acceptType, router);
    }

    /**
     * Add a GET handler
     */
    public void addGetRoute(String path, String acceptType, Route router) {
        get(path, acceptType, router);
    }
}
