package dev.avyguzov.api;

import dev.avyguzov.ApiClientTester;
import dev.avyguzov.ConfigsReader;
import dev.avyguzov.Main;
import dev.avyguzov.db.DataBase;
import dev.avyguzov.db.DataBaseImpl;
import org.junit.jupiter.api.*;

import java.io.IOException;

/**
 * Check API calls
 */
public class CheckFactorialIntegrationTest {
    private final ApiClientTester client = new ApiClientTester();
    private final ConfigsReader configsReader = new ConfigsReader("application-test.properties");
    private final String serverHost = configsReader.getProperty("server.host");

    public CheckFactorialIntegrationTest() throws IOException {}

    @BeforeEach
    public void setUp() {
        Main.main(new String[] {"test"});
    }

    @Test
    public void checkFactorial() throws Exception {
        System.out.println("Start - CheckFactorialIntegrationTest:checkFactorial test");
        client.computeFactorial(serverHost + "/add-slow-factorial-task", "5");
        client.computeFactorial(serverHost + "/add-slow-factorial-task", "6");
        client.computeFactorial(serverHost + "/add-slow-factorial-task", "7");
        Thread.sleep(5000);
        String factorial5 = client.checkTask(serverHost + "/check-task", "5");
        String factorial6 = client.checkTask(serverHost + "/check-task", "6");

        Assertions.assertEquals(120, Integer.parseInt(factorial5));
        Assertions.assertEquals(720, Integer.parseInt(factorial6));
    }

    @AfterEach
    public void tearDown() {
        if (Main.globalInjector != null) {
            DataBase db = Main.globalInjector.getInstance(DataBaseImpl.class);
            db.clearDb();
        }
    }
}
