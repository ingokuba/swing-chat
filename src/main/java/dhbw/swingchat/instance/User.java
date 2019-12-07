package dhbw.swingchat.instance;

import java.util.ArrayList;
import java.util.List;

/**
 * User object identified by it's name.
 */
public class User
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
        messages.add(message);
    }

    public List<String> getMessages()
    {
        return messages;
    }
}
