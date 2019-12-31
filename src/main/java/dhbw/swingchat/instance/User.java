package dhbw.swingchat.instance;

import static java.util.Objects.hash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.google.gson.annotations.Expose;

import dhbw.swingchat.helper.ChangeEvent;

/**
 * User object identified by it's name.
 */
public class User extends Observable
    implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Expose
    private List<String>      messages         = new ArrayList<>();
    @Expose
    private String            name;

    public User(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Adds a message to the list.
     */
    public User message(String message)
    {
        messages.add(message);
        setChanged();
        notifyObservers(ChangeEvent.add(message));
        return this;
    }

    public List<String> getMessages()
    {
        return messages;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof User) {
            User user = (User)obj;
            return getName().equals(user.getName());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return hash(name);
    }
}
