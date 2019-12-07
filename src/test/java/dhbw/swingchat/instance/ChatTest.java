package dhbw.swingchat.instance;

import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        chat.add(new User("test"));

        assertFalse(chat.absent("test"));
    }

    @Test
    public void should_message_user()
    {
        User user = new User("test");
        chat.add(user);

        chat.message("msgTest", "test");

        assertThat(user.getMessages(), hasItem("msgTest"));
    }

    @Test
    public void should_only_message_given_user()
    {
        User user1 = new User("test1");
        chat.add(user1);
        User user2 = new User("test2");
        chat.add(user2);

        chat.message("msgTest", "test1");

        assertThat(user1.getMessages(), hasItem("msgTest"));
        assertThat(user2.getMessages(), empty());
    }

    @Test
    public void should_not_put_duplicate_user()
    {
        User user1 = new User("test");
        chat.add(user1);
        chat.message("msgTest", "test");

        User user2 = new User("test");
        chat.add(user2);

        User existing = chat.getUsers().get("test");
        assertThat(existing.getMessages(), hasItem("msgTest"));
    }
}
