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
        updateUsers();
    }

    public String getName()
    {
        return name;
    }

    /**
     * Set back references in the {@link User} objects.
     */
    private void updateUsers()
    {
        users.forEach(user -> user.addGroup(this));
    }

    /**
     * Remove user from the group.
     */
    public Group remove(User user)
    {
        if (users.remove(user)) {
            user.removeGroup(this);
        }
        return this;
    }
}
