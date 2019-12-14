package dhbw.swingchat.instance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.google.gson.annotations.Expose;

import dhbw.swingchat.ChangeMode;

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
        if (messages.add(message)) {
            setChanged();
            notifyObservers(ChangeMode.MESSAGE);
        }
        return this;
    }

    public List<String> getMessages()
    {
        return messages;
    }
}
