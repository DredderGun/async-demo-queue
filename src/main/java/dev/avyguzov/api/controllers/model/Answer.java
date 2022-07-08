package dev.avyguzov.api.controllers.model;

import static dev.avyguzov.api.controllers.AbstractRequestController.OK;

/**
 * HTTP response
 */
public class Answer {
    /**
     * Response body
     */
    private String payload = "";

    /**
     * HTTP code
     */
    private Integer code = OK;

    public Answer(Integer code) {
        this.code = code;
    }

    public Answer(String payload) {
        this.payload = payload;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
