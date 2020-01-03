package dhbw.swingchat.instance;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Adds support for {@link PropertyChangeListener}s to a class.
 */
public abstract class ListenerSupport
{

    private PropertyChangeSupport changes;

    private PropertyChangeSupport getChanges()
    {
        if (changes == null) {
            changes = new PropertyChangeSupport(this);
        }
        return changes;
    }

    /**
     * Add a listener to the collection.
     */
    public final void addPropertyChangeListener(PropertyChangeListener l)
    {
        getChanges().addPropertyChangeListener(l);
    }

    protected final void firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        getChanges().firePropertyChange(propertyName, oldValue, newValue);
    }
}
