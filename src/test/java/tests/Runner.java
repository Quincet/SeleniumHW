package tests;

import app.Picker;
import org.junit.AfterClass;
import org.junit.Before;

public abstract class Runner {

    public static ThreadLocal<Picker> threadManager = new ThreadLocal<>();
    protected static Picker picker;

    @Before
    public void setUp() {
        if (threadManager.get() != null) {
            picker = threadManager.get();
            return;
        }
        picker = new Picker();
        threadManager.set(picker);
    }
    /*@After // используется в режиме без переиспользования браузера
    public void close(){
        picker.quit();
        threadManager.remove();
    }*/

    @AfterClass
    public static void tearDown() {
        picker.quit();
    }
}
