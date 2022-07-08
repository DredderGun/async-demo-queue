package dev.avyguzov.queue.tasks;

import dev.avyguzov.queue.TaskResult;
import dev.avyguzov.queue.exceptions.IncorrectArg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * Task mock. For demonstration purposes.
 * Compute factorial of n (n!)
 */
public class SlowFactorial implements Callable<TaskResult> {

    private static final Logger logger = LogManager.getLogger(SlowFactorial.class);
    private final String key;
    private final Integer n;

    public SlowFactorial(String key, Integer n) {
        this.key = key;
        this.n = n;
    }

    /**
     * n!
     * @return Computing result
     */
    @Override
    public TaskResult call() {
        if (n < 0) {
            throw new IncorrectArg();
        }
        logger.info("Computing factorial n= {}", n);
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
            // искусственно делаем задачу медленной
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        TaskResult tr = new TaskResult(key);
        tr.setError(false);
        tr.setResult(String.valueOf(result));
        return tr;
    }
}
