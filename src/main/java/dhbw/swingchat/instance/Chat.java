package dhbw.swingchat.instance;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * Collection of all users and their names.
 */
public class Chat
        extends ListenerSupport
{

    @Expose
    private List<User>  users  = new ArrayList<>();
    @Expose
    private List<Group> groups = new ArrayList<>();

    /**
     * Adds a new user to the collection.
     */
    public Chat addUser(User user)
    {
        if (!users.contains(user)) {
            users.add(user);
            firePropertyChange("users", null, user);
        }
        return this;
    }

    /**
     * Remove a user from the collection.
     */
    public Chat removeUser(User user)
    {
        if (users.remove(user)) {
            firePropertyChange("users", user, null);
        }
        return this;
    }

    public List<User> getUsers()
    {
        return users;
    }

    /**
     * Find user by name.
     * 
     * @param name Username to look for.
     * @return User object if found, else null.
     */
    public User getUser(String name)
    {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Message all users specified by the given names.
     */
    public Chat message(String message, String... usernames)
    {
        for (String username : usernames) {
            getUser(username).message(message);
        }
        return this;
    }

    /**
     * Add a group to the list. Notifies observers with the added group.
     */
    public Chat addGroup(Group group)
    {
        groups.add(group);
        firePropertyChange("groups", null, group);
        return this;
    }

    /**
     * Removes a group from the list. Notifies observers with the removed group.
     */
    public Chat removeGroup(Group group)
    {
        if (groups.remove(group)) {
            firePropertyChange("groups", group, null);
        }
        return this;
    }

    public List<Group> getGroups()
    {
        return groups;
    }

    /**
     * Find a group in this chat.
     * 
     * @return The existing instance in the list.
     */
    public Group getGroup(Group group)
    {
        for (Group existing : groups) {
            if (existing.equals(group)) {
                return existing;
            }
        }
        return null;
    }
}
