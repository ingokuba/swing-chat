package dhbw.swingchat.instance;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Collection of all users and their names.
 */
public class Chat extends Observable
{

    private Map<String, User> users = new HashMap<>();

    /**
     * Checks whether user with a given name exists.
     * 
     * @return true if it <b>doesn't</b> exist
     */
    public boolean absent(String name)
    {
        return users.get(name) == null;
    }

    /**
     * Adds a new user to the collection.
     */
    public Chat add(User user)
    {
        if (users.putIfAbsent(user.getName(), user) == null) {
            setChanged();
            notifyObservers();
        }
        return this;
    }

    /**
     * Remove a user from the collection.
     */
    public Chat remove(User user)
    {
        if (users.remove(user.getName()) != null) {
            setChanged();
            notifyObservers();
        }
        return this;
    }

    public Map<String, User> getUsers()
    {
        return users;
    }

    /**
     * Message all users specified by the given names.
     */
    public Chat message(String message, String... usernames)
    {
        for (String username : usernames) {
            users.get(username).message(message);
        }
        return this;
    }
}
