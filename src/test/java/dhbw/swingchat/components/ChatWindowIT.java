package dhbw.swingchat.components;

import static java.awt.event.KeyEvent.VK_ENTER;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.assertj.swing.core.matcher.JLabelMatcher.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JList;

import org.assertj.swing.core.matcher.JButtonMatcher;
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
    private Chat         chat = new Chat().addUser(user).addUser(new User("Second"));

    @BeforeEach
    public void setup()
    {
        ChatWindow component = GuiActionRunner.execute(() -> new ChatWindow(user, chat));
        chatWindow = new FrameFixture(component);
        chatWindow.show();
    }

    @AfterEach
    public void cleanup()
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
        chatWindow.textBox("userInput").requireText("");
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
        chatWindow.button("newGroup").click();

        chatWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);

        assertThat(chat.getGroups(), hasSize(1));
        chatWindow.button("Groupie").requireVisible().requireText("Groupie").requireToolTip("Chatter, Second");
    }

    @Test
    public void should_not_create_group_without_users()
    {
        chatWindow.checkBox("Chatter").uncheck();
        chatWindow.checkBox("Second").uncheck();

        chatWindow.button("newGroup").click();

        assertThat(chat.getGroups(), hasSize(0));
        chatWindow.dialog().label(withText("Group cannot be empty")).requireVisible();
    }

    @Test
    public void should_not_create_group_without_name()
    {
        chatWindow.button("newGroup").click();

        chatWindow.dialog().button(JButtonMatcher.withText("OK")).click();

        assertThat(chat.getGroups(), hasSize(0));
        chatWindow.dialog().label(withText("Group name cannot be empty")).requireVisible();
    }

    @Test
    public void should_not_create_group_with_same_name_and_members()
    {
        chatWindow.button("newGroup").click();
        chatWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);

        chatWindow.button("newGroup").click();
        chatWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);

        assertThat(chat.getGroups(), hasSize(1));
        chatWindow.dialog().label(withText("Same group already exists")).requireVisible();
    }

    @Test
    public void should_remove_group_on_close()
    {
        chatWindow.checkBox("Second").click();
        chatWindow.button("newGroup").click();
        chatWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);
        assertThat(chat.getGroups(), hasSize(1));

        chatWindow.close();

        assertThat(chat.getGroups(), hasSize(0));
    }

    @Test
    public void should_select_users_in_group()
    {
        chatWindow.checkBox("Second").click();
        chatWindow.button("newGroup").click();
        chatWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);

        chatWindow.checkBox("Chatter").click();
        chatWindow.button("Groupie").click();

        chatWindow.checkBox("Chatter").requireSelected();
        chatWindow.checkBox("Second").requireNotSelected();
    }

    @Test
    public void should_have_button_icon()
    {
        assertThat(chatWindow.button("newGroup").target().getIcon(), notNullValue());
    }

    @Test
    public void should_have_button_label_when_image_cannot_be_loaded()
        throws IOException
    {
        File file = new File(getClass().getResource("/img/group_add.png").getFile());
        Path temp = Files.move(file.toPath(), file.toPath().resolveSibling("temp"), REPLACE_EXISTING);
        try {
            cleanup();
            setup();
            assertThat(chatWindow.button("newGroup").target().getText(), is("New group"));
        } finally {
            Files.move(temp, file.toPath());
        }
    }
}
