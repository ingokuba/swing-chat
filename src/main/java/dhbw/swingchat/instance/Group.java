package dhbw.swingchat.instance;

import java.util.List;

public class Group
{

    private String     name;
    private List<User> users;

    public Group(String name, List<User> users)
    {
        this.name = name;
        this.users = users;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Remove user from the group.
     */
    public Group remove(User user)
    {
        users.remove(user);
        return this;
    }

    /**
     * Check whether user is in this group.
     */
    public boolean contains(User user)
    {
        return users.contains(user);
    }

    public boolean isEmpty()
    {
        return users.isEmpty();
    }
}
