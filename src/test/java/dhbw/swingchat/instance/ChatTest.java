package dhbw.swingchat.instance;

import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dhbw.swingchat.ChangeMode;
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
        assertThat(chat.getUsers(), anEmptyMap());
    }

    @Test
    public void should_return_true_for_absent_user()
    {
        assertTrue(chat.absent("test"));
    }

    @Test
    public void should_return_false_for_existing_user()
    {
        chat.addUser(new User("test"));

        assertFalse(chat.absent("test"));
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

        User existing = chat.getUsers().get("test");
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
        List<User> users = new ArrayList<>();
        users.add(new User("test"));
        assertFalse(observer.getCalled());

        chat.addGroup(new Group("Admins", users));

        assertTrue(observer.getCalled());
        assertThat(observer.getObject(), is(ChangeMode.GROUP));
    }
}
