package dev.avyguzov.api.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation for controllers
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerPath {

    /**
     * http verb POST, GET etc
     */
    String method();

    /**
     * Path to your controller
     */
    String value();

    /**
     * Request type
     */
    String acceptType() default "application/json";
}
