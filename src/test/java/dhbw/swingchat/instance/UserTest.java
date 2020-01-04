package dhbw.swingchat.instance;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.beans.PropertyChangeEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dhbw.swingchat.test.TestListener;

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
    public void should_notify_listener_when_message_added()
    {
        TestListener listener = new TestListener();
        user.addPropertyChangeListener(listener);

        String message = "Test message";
        user.message(message);

        PropertyChangeEvent event = listener.getEvent();
        assertNotNull(event);
        assertThat(event.getPropertyName(), is("messages"));
        assertThat(event.getOldValue(), nullValue());
        assertThat(event.getNewValue(), is(message));
    }

    @Test
    public void should_not_equal_another_instance()
    {
        assertThat(new User("Peter"), not(equalTo(new Object())));
    }
}
