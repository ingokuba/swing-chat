package dhbw.swingchat.components;

import static java.awt.event.KeyEvent.VK_ENTER;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.swing.JDialog;
import javax.swing.JList;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.exception.WaitTimedOutError;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
    private Chat         chat = new Chat().addUser(user).addUser(new User("Second"));

    @BeforeEach
    public void setup()
    {
        ChatWindow component = GuiActionRunner.execute(() -> new ChatWindow(user, chat));
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
        assertThat(chat.getUsers(), hasSize(2));

        chatWindow.close();

        assertThat(chat.getUsers(), hasSize(1));
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
        chatWindow.checkBox("Chatter").uncheck();

        chatWindow.textBox("userInput").enterText("Test message").pressAndReleaseKeys(VK_ENTER);

        chatWindow.list("messages").requireItemCount(0);
    }

    @Test
    public void should_create_group()
    {
        chatWindow.button().click();

        chatWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);

        assertThat(chat.getGroups(), hasSize(1));
        chatWindow.button("Groupie").requireVisible();
    }

    @Test
    public void should_not_create_group_without_users()
    {
        chatWindow.checkBox("Chatter").uncheck();
        chatWindow.checkBox("Second").uncheck();
        chatWindow.button().click();
        Assertions.assertThrows(WaitTimedOutError.class, () -> {
            WindowFinder.findDialog(JDialog.class).withTimeout(100).using(chatWindow.robot());
        });
        assertThat(chat.getGroups(), hasSize(0));
    }

    @Test
    @Disabled("Somehow dialog doesn't show up in this test case.")
    public void should_not_create_group_without_name()
    {
        chatWindow.button().click();

        chatWindow.dialog().textBox().pressAndReleaseKeys(VK_ENTER);

        assertThat(chat.getGroups(), hasSize(0));
    }

    @Test
    public void should_remove_group_on_close()
    {
        chatWindow.checkBox("Second").click();
        chatWindow.button().click();
        chatWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);
        assertThat(chat.getGroups(), hasSize(1));

        chatWindow.close();

        assertThat(chat.getGroups(), hasSize(0));
    }

    @Test
    public void should_select_users_in_group()
    {
        chatWindow.checkBox("Second").click();
        chatWindow.button().click();
        chatWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);

        chatWindow.checkBox("Chatter").click();
        chatWindow.button("Groupie").click();

        chatWindow.checkBox("Chatter").requireSelected();
        chatWindow.checkBox("Second").requireNotSelected();
    }
}
