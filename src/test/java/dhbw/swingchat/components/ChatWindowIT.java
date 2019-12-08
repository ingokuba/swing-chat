package dhbw.swingchat.components;

import static java.awt.event.KeyEvent.VK_ENTER;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.swing.JList;

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

    private ChatWindow   component;
    private FrameFixture chatWindow;

    private User         user = new User("Chatter");
    private Chat         chat = new Chat().add(user);

    @BeforeEach
    public void setup()
    {
        component = GuiActionRunner.execute(() -> new ChatWindow(user, chat));
        chatWindow = new FrameFixture(component);
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

    @Test
    public void should_remove_user_on_close()
    {
        assertThat(chat.getUsers(), aMapWithSize(1));

        chatWindow.close();

        assertThat(chat.getUsers(), anEmptyMap());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_add_message_to_list()
    {
        chatWindow.textBox("userInput").enterText("Test message").pressAndReleaseKeys(VK_ENTER);

        chatWindow.list("messages").requireItemCount(1);
        JList<String> messageList = chatWindow.list("messages").target();
        assertThat(messageList.getModel().getElementAt(0), containsString("Test message"));
    }

    @Test
    public void should_not_add_message_to_list_for_deselected_user()
    {
        chatWindow.checkBox().check(false);

        chatWindow.textBox("userInput").enterText("Test message").pressAndReleaseKeys(VK_ENTER);

        chatWindow.list("messages").requireItemCount(0);
    }
}
