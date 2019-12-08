package dhbw.swingchat.instance;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dhbw.swingchat.ChangeMode;
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

        user.message("Test message");

        assertTrue(observer.getCalled());
        assertThat(observer.getObject(), is(ChangeMode.MESSAGE));
    }
}
