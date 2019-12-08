package dhbw.swingchat.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * User object identified by it's name.
 */
public class User extends Observable
{

    private List<String> messages = new ArrayList<>();

    private String       name;

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
    public void message(String message)
    {
        if (messages.add(message)) {
            setChanged();
            notifyObservers();
        }
    }

    public List<String> getMessages()
    {
        return messages;
    }
}
