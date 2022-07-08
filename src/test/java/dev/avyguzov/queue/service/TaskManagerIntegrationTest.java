package dev.avyguzov.queue.service;

import dev.avyguzov.Main;
import dev.avyguzov.db.DataBase;
import dev.avyguzov.db.DataBaseImpl;
import dev.avyguzov.queue.tasks.SlowFactorial;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * TaskManager integration tests.
 */
public class TaskManagerIntegrationTest {
    
    @BeforeEach
    public void setUp() {
        Main.main(new String[] {"test"});
    }


    /**
     * Trying to start many tasks simultaneously
     */
    @Test
    public void manyThreadsTryingToAddTask() throws InterruptedException {
        System.out.println("Start - TaskManagerIntegrationTest:manyThreadsTryingToAddTask test");

        TaskManager taskManager = Main.globalInjector.getInstance(TaskManager.class);
        CustomThreadPoolExecutor threadPoolExecutor = Main.globalInjector.getInstance(CustomThreadPoolExecutor.class);

        final int parallelRequestsCount = 20;
        int maxSecondsForWait = 80; // max seconds for waiting tasks complete
        final List<Callable<Boolean>> requests = new ArrayList<>();
        final ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < parallelRequestsCount; i++) {
            final int currNmbr = i;
            requests.add(() -> {
                taskManager.addTask(new FutureTask<>(new SlowFactorial(String.valueOf(currNmbr), currNmbr)));
                return true;
            });
        }

        // start all tasks simultaneously
        executorService.invokeAll(requests);
        // and add one another task
        taskManager.addTask(new FutureTask<>(new SlowFactorial(String.valueOf(30), 30)));

        // wait until all tasks are completed
        LocalDateTime startTime = LocalDateTime.now();
        while (
                threadPoolExecutor.getCompletedTaskCount() != parallelRequestsCount + 1 &&
                LocalDateTime.now().isBefore(startTime.plusSeconds(maxSecondsForWait)) // break loop if it froze
        ) { }

        // check results
        Assertions.assertNotEquals("", taskManager.checkTask("30"));
        Assertions.assertNotEquals("", taskManager.checkTask("1"));
        Assertions.assertNotEquals("", taskManager.checkTask("5"));
        Assertions.assertNotEquals("", taskManager.checkTask("7"));
        Assertions.assertNotEquals("", taskManager.checkTask("15"));
        Assertions.assertNotEquals("", taskManager.checkTask("19"));
    }

    @AfterEach
    public void tearDown() {
        if (Main.globalInjector != null) {
            DataBase db = Main.globalInjector.getInstance(DataBaseImpl.class);
            db.clearDb();
        }
    }
}
