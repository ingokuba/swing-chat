package dhbw.swingchat.test;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.util.Observable;
import java.util.Observer;

public class TestObserver
    implements Observer
{

    private Class<?> type;
    private boolean  called = false;
    private Object   object;

    public TestObserver(Class<?> type)
    {
        this.type = type;
    }

    @Override
    public void update(Observable o, Object obj)
    {
        called = true;
        object = obj;
        assertThat(o, instanceOf(type));
    }

    public boolean getCalled()
    {
        return called;
    }

    public Object getObject()
    {
        return object;
    }
}
