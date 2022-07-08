package dev.avyguzov.api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.avyguzov.api.config.HandlerPath;
import dev.avyguzov.api.controllers.model.Answer;
import dev.avyguzov.db.DataBase;
import dev.avyguzov.queue.service.TaskManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

@HandlerPath(method = "POST", value = "/check-task")
public class CheckTaskController extends AbstractRequestController<String> {
    private static final Logger logger = LogManager.getLogger(CheckTaskController.class);
    private final TaskManager taskManager;

    @Inject
    public CheckTaskController(DataBase db, ObjectMapper objectMapper, TaskManager taskManager) {
        super(db, objectMapper, new TypeReference<>() {});
        this.taskManager = taskManager;
    }

    @Override
    protected Answer processImpl(String id) {
        logger.info("Request for check task with id = {}", id);
        return new Answer(taskManager.checkTask(id));
    }

    @Override
    protected boolean isValid(String n) {
        return true;
    }
}
