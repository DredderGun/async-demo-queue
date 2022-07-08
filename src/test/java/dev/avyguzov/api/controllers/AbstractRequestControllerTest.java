package dev.avyguzov.api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.avyguzov.api.controllers.model.Answer;
import dev.avyguzov.db.DataBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static dev.avyguzov.api.controllers.AbstractRequestController.HTTP_BAD_REQUEST;

/**
 * Unit test for common AbstractRequestController.
 */
class AbstractRequestControllerTest {

    @Mock
    private DataBase dataBase;
    private final Integer failedNmbr = 1;
    private final Integer successCode = 200;
    private final String successMsg = "success";

    AbstractRequestController<Integer> rq = new AbstractRequestController<>(dataBase, new ObjectMapper(), new TypeReference<Integer>() {}) {
        @Override
        protected Answer processImpl(Integer value) {
            Answer answer = new Answer(successCode);
            answer.setPayload(successMsg);
            return answer;
        }

        @Override
        protected boolean isValid(Integer value) {
            return !value.equals(failedNmbr);
        }
    };


    @Test
    void whenNotValidReturnBadAnswer() {
        Answer answer = rq.process(failedNmbr);

        Assertions.assertEquals(HTTP_BAD_REQUEST, answer.getCode());
    }

    @Test
    void whenValidPassHandleToChild() {
        Answer answer = rq.process(4);

        Assertions.assertEquals(successCode, answer.getCode());
        Assertions.assertEquals(successMsg, answer.getPayload());
    }
}