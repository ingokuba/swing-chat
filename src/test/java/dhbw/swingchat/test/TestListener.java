package dhbw.swingchat.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TestListener
    implements PropertyChangeListener
{

    private PropertyChangeEvent event;

    public PropertyChangeEvent getEvent()
    {
        return event;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        this.event = event;
    }
}
