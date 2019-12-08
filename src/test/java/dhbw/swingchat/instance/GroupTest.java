package dhbw.swingchat.instance;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Group} object.
 */
public class GroupTest
{

    @Test
    public void should_add_group_to_user()
    {
        User user = new User("Tester");
        List<User> users = new ArrayList<>();
        users.add(user);

        new Group("Admins", users);

        assertThat(user.getGroups(), hasSize(1));
    }
}
