package tests;

import app.FactoryPages;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Runner {

    private static ThreadLocal<FactoryPages> threadManager = new ThreadLocal<>();
    protected static FactoryPages factoryPages;
    private Logger logger = LoggerFactory.getLogger(Runner.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description){
            logger.info(String.format("Тест кейс %s начал свою работу",description.getMethodName()));
        }
        protected void finished(Description description){
            logger.info(String.format("Тест кейс %s закончил свою работу",description.getMethodName()));
        }
    };

    @Before
    public void setUp() {
        if (threadManager.get() != null) {
            factoryPages = threadManager.get();
            return;
        }
        factoryPages = new FactoryPages();
        threadManager.set(factoryPages);
    }
    /*@After // используется в режиме без переиспользования браузера
    public void close(){
        factoryPages.quit();
        threadManager.remove();
    }*/

    @AfterClass
    public static void tearDown() {
        factoryPages.quit();
    }
}
