package tests;

import app.FactoryPages;
import org.junit.AfterClass;
import org.junit.Before;

public abstract class Runner {

    private static ThreadLocal<FactoryPages> threadManager = new ThreadLocal<>();
    protected static FactoryPages factoryPages;

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
