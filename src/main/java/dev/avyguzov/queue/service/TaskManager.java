package dev.avyguzov.queue.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.avyguzov.db.DataBase;
import dev.avyguzov.db.DataBaseImpl;
import dev.avyguzov.queue.TaskResult;

import java.util.concurrent.FutureTask;

/**
 * Responsible for all interaction with tasks: read from DB, add new tasks to threadPoolExecutor
 */
@Singleton
public class TaskManager {
    private final CustomThreadPoolExecutor threadPoolExecutor;
    private final DataBase dataBase;

    @Inject
    public TaskManager(CustomThreadPoolExecutor threadPoolExecutor, DataBaseImpl dataBase) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.dataBase = dataBase;
    }

    public void addTask(FutureTask<TaskResult> task) {
        threadPoolExecutor.execute(task);
    }

    public String checkTask(String key) {
        return dataBase.read(key);
    }
}
