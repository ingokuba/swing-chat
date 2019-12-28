package dhbw.swingchat.instance;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dhbw.swingchat.helper.ChangeEvent;
import dhbw.swingchat.helper.ChangeMode;
import dhbw.swingchat.test.TestObserver;

/**
 * Unit tests for the {@link User} object.
 */
public class UserTest
{

    private User user;

    @BeforeEach
    public void setup()
    {
        user = new User("Tester");
    }

    @Test
    public void should_notify_observer_when_message_added()
    {
        TestObserver observer = new TestObserver(User.class);
        user.addObserver(observer);

        String message = "Test message";
        user.message(message);

        assertTrue(observer.getCalled());
        ChangeEvent event = (ChangeEvent)observer.getObject();
        assertThat(event.getMode(), is(ChangeMode.ADD));
        assertThat(event.getObject(), is(message));
    }
}
