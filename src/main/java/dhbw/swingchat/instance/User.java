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

    private List<Group>  groups   = new ArrayList<>();

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
            notifyObservers(ChangeMode.MESSAGE);
        }
    }

    public List<String> getMessages()
    {
        return messages;
    }

    public User addGroup(Group group)
    {
        if (groups.add(group)) {
            setChanged();
            notifyObservers(ChangeMode.GROUP);
        }
        return this;
    }

    public User removeGroup(Group group)
    {
        if (groups.remove(group)) {
            setChanged();
            notifyObservers(ChangeMode.GROUP);
        }
        return this;
    }

    public List<Group> getGroups()
    {
        return groups;
    }

    public enum ChangeMode
    {
        /**
         * Message was added.
         */
        MESSAGE,
        /**
         * Group was added.
         */
        GROUP;
    }
}
