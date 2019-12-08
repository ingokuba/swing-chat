package dhbw.swingchat.instance;

import static java.util.Arrays.asList;

import java.util.List;

import com.google.gson.annotations.Expose;

public class Group
{

    @Expose
    private String     name;
    @Expose
    private List<User> users;

    public Group(String name, List<User> users)
    {
        this.name = name;
        this.users = users;
    }

    public Group(String name, User... users)
    {
        this(name, asList(users));
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
        return contains(user.getName());
    }

    public boolean contains(String name)
    {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty()
    {
        return users.isEmpty();
    }
}
