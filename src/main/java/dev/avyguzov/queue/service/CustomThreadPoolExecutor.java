package dev.avyguzov.queue.service;

import dev.avyguzov.db.DataBase;
import dev.avyguzov.queue.TaskResult;
import dev.avyguzov.queue.exceptions.IncorrectResultFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

/**
 * This class are responsible for all work with tasks execution.
 */
public class CustomThreadPoolExecutor extends ThreadPoolExecutor {

    private static final Logger logger = LogManager.getLogger(CustomThreadPoolExecutor.class);

    private final DataBase dataBase;
    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                    TimeUnit unit, BlockingQueue<Runnable> workQueue, DataBase dataBase) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.dataBase = dataBase;
    }

    /**
     * Handles tasks results.
     */
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t == null && r instanceof Future<?>) {
            try {
                Object result = ((Future<?>) r).get();
                if (result instanceof TaskResult) {
                    dataBase.write(((TaskResult) result).getId(), ((TaskResult) result).getResult());
                } else {
                    throw new IncorrectResultFormat();
                }
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException | IncorrectResultFormat ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt(); // ignore/reset
            }
        }
        if (t != null) {
            System.out.println(t);
            logger.error(t);
        }
    }
}
