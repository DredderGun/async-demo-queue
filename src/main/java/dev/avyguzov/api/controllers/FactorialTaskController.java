package dev.avyguzov.api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.avyguzov.api.config.HandlerPath;
import dev.avyguzov.api.controllers.model.Answer;
import dev.avyguzov.db.DataBase;
import dev.avyguzov.queue.service.TaskManager;
import dev.avyguzov.queue.tasks.SlowFactorial;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.concurrent.FutureTask;

/**
 * Concrete controller for the demo factorial task.
 * Factorial task compute factorial very slowly. Only for demonstration purpose.
 */
@HandlerPath(method = "POST", value = "/add-slow-factorial-task")
public class FactorialTaskController extends AbstractRequestController<Integer> {
    private static final Logger logger = LogManager.getLogger(FactorialTaskController.class);
    private final TaskManager taskManager;

    @Inject
    public FactorialTaskController(DataBase db, ObjectMapper objectMapper, TaskManager taskManager) {
        super(db, objectMapper, new TypeReference<>() {});
        this.taskManager = taskManager;
    }

    @Override
    protected Answer processImpl(Integer n) {
        logger.info("Request for factorial task n = {}", n);
        taskManager.addTask(new FutureTask<>(new SlowFactorial(String.valueOf(n), n)));
        return new Answer("Task sent");
    }

    @Override
    protected boolean isValid(Integer n) {
        return n >= 0;
    }
}
