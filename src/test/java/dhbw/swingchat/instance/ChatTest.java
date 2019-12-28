package dhbw.swingchat.instance;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dhbw.swingchat.helper.ChangeEvent;
import dhbw.swingchat.helper.ChangeMode;
import dhbw.swingchat.test.TestObserver;

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
    public void should_notify_observer_when_user_added()
    {
        TestObserver observer = new TestObserver(Chat.class);
        chat.addObserver(observer);

        chat.addUser(new User("test"));

        assertTrue(observer.getCalled());
    }

    @Test
    public void should_notify_observer_when_user_removed()
    {
        User user = new User("test");
        chat.addUser(user);
        TestObserver observer = new TestObserver(Chat.class);
        chat.addObserver(observer);
        assertFalse(observer.getCalled());

        chat.removeUser(user);

        assertTrue(observer.getCalled());
    }

    @Test
    public void should_notify_observer_when_group_added()
    {
        TestObserver observer = new TestObserver(Chat.class);
        chat.addObserver(observer);
        assertFalse(observer.getCalled());

        Group group = new Group("Admins", new User("test"));
        chat.addGroup(group);

        assertTrue(observer.getCalled());
        ChangeEvent event = (ChangeEvent)observer.getObject();
        assertThat(event.getMode(), is(ChangeMode.ADD));
        assertThat(event.getObject(), is(group));
        assertThat(chat.getGroups(), hasSize(1));
    }

    @Test
    public void should_notify_observer_when_group_removed()
    {
        Group group = new Group("Admins", new User("test"));
        chat.addGroup(group);
        TestObserver observer = new TestObserver(Chat.class);
        chat.addObserver(observer);
        assertFalse(observer.getCalled());

        chat.removeGroup(group);

        assertTrue(observer.getCalled());
        ChangeEvent event = (ChangeEvent)observer.getObject();
        assertThat(event.getMode(), is(ChangeMode.REMOVE));
        assertThat(event.getObject(), is(group));
    }

    @Test
    public void should_not_notify_observer_when_group_is_not_removed()
    {
        Group group = new Group("Admins", new User("test"));
        TestObserver observer = new TestObserver(Chat.class);
        chat.addObserver(observer);

        chat.removeGroup(group);

        assertFalse(observer.getCalled());
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
    public void should_not_notify_observer_when_user_is_not_removed()
    {
        chat.addUser(new User("Peter"));
        TestObserver observer = new TestObserver(Chat.class);
        chat.addObserver(observer);

        chat.removeUser(new User("None"));

        assertFalse(observer.getCalled());
    }
}
