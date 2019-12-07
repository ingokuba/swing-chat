package dhbw.swingchat.components;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dhbw.swingchat.instance.Chat;
import dhbw.swingchat.instance.User;

/**
 * UI test for the {@link ChatWindow}.
 */
public class ChatWindowIT
{

    private FrameFixture chatWindow;

    private User         user = new User("Chatter");
    private Chat         chat = new Chat();

    @BeforeEach
    public void setup()
    {
        ChatWindow frame = GuiActionRunner.execute(() -> new ChatWindow(user, chat));
        chatWindow = new FrameFixture(frame);
        chatWindow.show();
    }

    @AfterEach
    private void cleanup()
    {
        chatWindow.cleanUp();
    }

    @Test
    public void should_have_username()
    {
        assertThat(chatWindow.target().getName(), is(user.getName()));
    }
}
