package dev.avyguzov.api.config;

import dev.avyguzov.api.controllers.RoutersRegistrant;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Set;

/**
 * RoutesMapping unit test
 */
public class RoutesMappingTest {
    @HandlerPath(method = "GET", value = "/my-route")
    private static class MyGetRoute implements Route {
        @Override
        public Object handle(Request request, Response response) {
            return null;
        }
    }
    @HandlerPath(method = "POST", value = "/my-route")
    private static class MyPostRoute implements Route {
        @Override
        public Object handle(Request request, Response response) {
            return null;
        }
    }
    private final RoutersRegistrant registrant = Mockito.mock(RoutersRegistrant.class);

    @Test
    public void routesMappingMustInitMyRoutesWithHandlerPathAnnotation() {
        MyGetRoute getRoute = new MyGetRoute();
        MyPostRoute postRoute = new MyPostRoute();

        new RoutesMapper(registrant, Set.of(getRoute, postRoute));

        Mockito.verify(registrant, Mockito.times(1))
                .addGetRoute("/my-route", "application/json", getRoute);
        Mockito.verify(registrant, Mockito.times(1))
                .addPostRoute("/my-route", "application/json", postRoute);
    }
}
