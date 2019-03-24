package utils;

import org.junit.AfterClass;
import org.junit.Before;

public abstract class Runner {

    private static ThreadLocal<Picker> threadManager = new ThreadLocal<>();
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

    @AfterClass
    public static void tearDown() {
        picker.quit();
    }
}
