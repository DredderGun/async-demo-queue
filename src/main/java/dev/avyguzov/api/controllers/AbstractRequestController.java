package dev.avyguzov.api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.avyguzov.api.controllers.model.Answer;
import dev.avyguzov.db.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Common stuff for all controllers. Receive http requests and deliver process to descendants.
 * @param <V> request payload type
 */
public abstract class AbstractRequestController<V> implements Route {
    private static final Logger logger = LogManager.getLogger(AbstractRequestController.class);

    public static final int HTTP_BAD_REQUEST = 400;
    public static final int OK = 200;
    protected final TypeReference<V> typeReference;
    protected final DataBase db;
    protected final ObjectMapper objectMapper;

    public AbstractRequestController(DataBase dataBase, ObjectMapper objectMapper, TypeReference<V> typeReference) {
        this.objectMapper = objectMapper;
        this.db = dataBase;
        this.typeReference = typeReference;
    }

    /**
     * Common process method for all controllers. It does all common stuff like handling
     * exceptions and controllers methods handling isValid and processImpl.
     * @param value request payload
     * @return Result of request handle
     */
    public final Answer process(V value) {
        if (!isValid(value)) {
            logger.info("Validation was failed for value: {}", value);
            Answer answer = new Answer(HTTP_BAD_REQUEST);
            answer.setPayload("Validation failed!");
            return answer;
        } else {
            try {
                return processImpl(value);
            } catch (Exception ex) {
                LogManager.getLogger("errors").error(ex.getStackTrace());
                Answer answer = new Answer(HTTP_BAD_REQUEST);
                answer.setPayload(ex.getMessage());
                ex.printStackTrace();
                return answer;
            }
        }
    }

    /**
     * Custom process for each controller
     * @param value request payload
     * @return process result
     */
    protected abstract Answer processImpl(V value) throws Exception;

    /**
     * Controller validation
     * @param value request payload
     * @return result
     */
    protected abstract boolean isValid(V value);

    /**
     * Spark route interceptor. Change Response object after request handle
     * @param request http request Spark object
     * @param response http response Spark object
     * @return Result of request handle
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String body = request.body();
        if (request.body() == null || request.body().isEmpty()) {
            body = "{\"payload\": \"\"}";
        }
        V paredBody = objectMapper.readValue(body, typeReference);
        Answer answer = process(paredBody);
        response.status(answer.getCode());
        response.type("application/json");
        response.body(answer.getPayload());
        return answer.getPayload();
    }

}