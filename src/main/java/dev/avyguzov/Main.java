package dev.avyguzov;

import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import dev.avyguzov.api.config.RoutesMapper;
import dev.avyguzov.api.controllers.CheckTaskController;
import dev.avyguzov.api.controllers.FactorialTaskController;
import dev.avyguzov.db.DataBase;
import dev.avyguzov.queue.service.CustomThreadPoolExecutor;
import spark.Route;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static spark.Spark.init;

/**
 * Start app with pattern
 *
 */
public class Main extends AbstractModule {
    public enum Profile {
        TEST, PROD
    }
    public static Profile currentProfile;
    public static Injector globalInjector;

    /**
     * Configuration bean
     * @return configs
     */
    @Provides
    static ConfigsReader getConfigsReader() throws IOException {
        return new ConfigsReader("application-" + currentProfile.name().toLowerCase() + ".properties");
    }

    @Provides @Singleton
    static CustomThreadPoolExecutor getCustomThreadPoolExecutor(ConfigsReader configsReader, DataBase db) {
        System.out.println("configsReader" + configsReader);
        int taskCount = Integer.parseInt(configsReader.getProperty("tasks-count"));
        boolean isAsync = Boolean.parseBoolean(configsReader.getProperty("isAsync"));

        if (!isAsync) {
            return new CustomThreadPoolExecutor(1, Integer.MAX_VALUE,
                    Long.MAX_VALUE, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>(), db);
        }

        return new CustomThreadPoolExecutor(taskCount, Integer.MAX_VALUE,
                        Long.MAX_VALUE, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>(), db);
    }

    /**
     *  Initialize routers for Guice manually
     */
    @Override
    public void configure() {
        Multibinder<Route> routeMultiBinder = Multibinder.newSetBinder(binder(), Route.class);
        routeMultiBinder.addBinding().to(FactorialTaskController.class);
        routeMultiBinder.addBinding().to(CheckTaskController.class);
        // Kick RoutesMapper to initializes all routes
        bind(RoutesMapper.class).asEagerSingleton();
    }

    /**
     * Method defines test or prod environment and start the app.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Prod profile is enabled");
            currentProfile = Profile.PROD;
        } else if (args[0].equalsIgnoreCase("test")) {
            System.out.println("Test profile is enabled");
            currentProfile = Profile.TEST;
        }
        init();
        globalInjector = Guice.createInjector(new Main());
    }
}
