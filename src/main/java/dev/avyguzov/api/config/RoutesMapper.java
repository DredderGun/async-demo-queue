package dev.avyguzov.api.config;

import dev.avyguzov.api.controllers.RoutersRegistrant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Route;

import javax.inject.Inject;
import java.util.*;

/**
 * Search and register all controllers with @HandlerPath annotation.
 */
public class RoutesMapper {
    private static final Logger logger = LogManager.getLogger(RoutesMapper.class);
    private final RoutersRegistrant registrant;
    private final Set<Route> routes;

    @Inject
    public RoutesMapper(RoutersRegistrant registrant, Set<Route> routes) {
        this.registrant = registrant;
        this.routes = routes;
        init();
    }

    public void init() {
        routes.forEach(route -> {
            HandlerPath handlerPathAnnotation = route.getClass().getAnnotation(HandlerPath.class);
            if (handlerPathAnnotation != null) {
                establishRoute(registrant, route);
            } else {
                logger.warn("All routes must have the HandlerPath annotation");
            }
        });
    }

    private void establishRoute(RoutersRegistrant registrant, Route route) {
        HandlerPath handlerPath = route.getClass().getAnnotation(HandlerPath.class);
        logger.info("Establishing route {} with method = {} and path = {}",
                route, handlerPath.method(), handlerPath.value());
        if (handlerPath.method().equalsIgnoreCase("get")) {
            registrant.addGetRoute(handlerPath.value(), handlerPath.acceptType(), route);
        } else if (handlerPath.method().equalsIgnoreCase("post")) {
            registrant.addPostRoute(handlerPath.value(), handlerPath.acceptType(), route);
        }
    }
}
