package dhbw.swingchat.instance;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.beans.PropertyChangeEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dhbw.swingchat.test.TestListener;

/**
 * Unit tests for the {@link Chat} object.
 */
public class ChatTest
{

    private Chat chat;

    @BeforeEach
    public void setup()
    {
        chat = new Chat();
    }

    @Test
    public void should_init_with_empty_collection()
    {
        assertThat(chat.getUsers(), empty());
    }

    @Test
    public void should_message_user()
    {
        User user = new User("test");
        chat.addUser(user);

        chat.message("msgTest", "test");

        assertThat(user.getMessages(), hasItem("msgTest"));
    }

    @Test
    public void should_only_message_given_user()
    {
        User user1 = new User("test1");
        chat.addUser(user1);
        User user2 = new User("test2");
        chat.addUser(user2);

        chat.message("msgTest", "test1");

        assertThat(user1.getMessages(), hasItem("msgTest"));
        assertThat(user2.getMessages(), empty());
    }

    @Test
    public void should_not_put_duplicate_user()
    {
        User user1 = new User("test");
        chat.addUser(user1);
        chat.message("msgTest", "test");

        User user2 = new User("test");
        chat.addUser(user2);

        User existing = chat.getUser("test");
        assertThat(existing.getMessages(), hasItem("msgTest"));
    }

    @Test
    public void should_notify_listener_when_user_added()
    {
        TestListener listener = new TestListener();
        chat.addPropertyChangeListener(listener);

        chat.addUser(new User("test"));

        assertNotNull(listener.getEvent());
    }

    @Test
    public void should_notify_listener_when_user_removed()
    {
        User user = new User("test");
        chat.addUser(user);
        TestListener listener = new TestListener();
        chat.addPropertyChangeListener(listener);
        assertNull(listener.getEvent());

        chat.removeUser(user);

        assertNotNull(listener.getEvent());
    }

    @Test
    public void should_notify_listener_when_group_added()
    {
        TestListener listener = new TestListener();
        chat.addPropertyChangeListener(listener);
        assertNull(listener.getEvent());

        Group group = new Group("Admins", new User("test"));
        chat.addGroup(group);

        PropertyChangeEvent event = listener.getEvent();
        assertThat(event.getPropertyName(), is("groups"));
        assertThat(event.getOldValue(), nullValue());
        assertThat(event.getNewValue(), is(group));
        assertThat(chat.getGroups(), hasSize(1));
    }

    @Test
    public void should_notify_listener_when_group_removed()
    {
        Group group = new Group("Admins", new User("test"));
        chat.addGroup(group);
        TestListener listener = new TestListener();
        chat.addPropertyChangeListener(listener);
        assertNull(listener.getEvent());

        chat.removeGroup(group);

        assertNotNull(listener.getEvent());
        PropertyChangeEvent event = listener.getEvent();
        assertThat(event.getPropertyName(), is("groups"));
        assertThat(event.getOldValue(), is(group));
        assertThat(event.getNewValue(), nullValue());
    }

    @Test
    public void should_not_notify_listener_when_group_is_not_removed()
    {
        Group group = new Group("Admins", new User("test"));
        TestListener listener = new TestListener();
        chat.addPropertyChangeListener(listener);

        chat.removeGroup(group);

        assertNull(listener.getEvent());
    }

    @Test
    public void should_find_group()
    {
        Group group = new Group("Admins", new User("test"));
        chat.addGroup(group);

        Group foundGroup = chat.getGroup(group);

        assertThat(foundGroup, equalTo(group));
    }

    @Test
    public void should_not_return_inexistent_group()
    {
        Group group = new Group("Admins", new User("test"));
        chat.addGroup(group);

        Group foundGroup = chat.getGroup(new Group("Nerds", new User("test")));

        assertThat(foundGroup, nullValue());
    }

    @Test
    public void should_not_add_user_when_already_in_there()
    {
        User user = new User("Test");

        chat.addUser(user).addUser(user);

        assertThat(chat.getUsers(), hasSize(1));
    }

    @Test
    public void should_return_null_for_inexistent_user()
    {
        chat.addUser(new User("Peter"));

        assertThat(chat.getUser("None"), nullValue());
    }

    @Test
    public void should_not_notify_listener_when_user_is_not_removed()
    {
        chat.addUser(new User("Peter"));
        TestListener listener = new TestListener();
        chat.addPropertyChangeListener(listener);

        chat.removeUser(new User("None"));

        assertNull(listener.getEvent());
    }
}
