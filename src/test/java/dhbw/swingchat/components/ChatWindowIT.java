package dhbw.swingchat.components;

import static java.awt.event.KeyEvent.VK_ENTER;
import static org.assertj.swing.core.matcher.JLabelMatcher.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JList;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JCheckBoxFixture;
import org.assertj.swing.fixture.JListFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import dhbw.swingchat.helper.ThemedColors;
import dhbw.swingchat.instance.Chat;
import dhbw.swingchat.instance.Group;
import dhbw.swingchat.instance.User;

/**
 * UI test for the {@link ChatWindow}.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ImageIO.class)
@PowerMockIgnore("javax.swing.*")
public class ChatWindowIT
{

    static final String         NEW_GROUP_BUTTON    = "newGroup";
    private static final String MESSAGES_LIST       = "messages";
    private static final String CHAT_INPUT          = "userInput";
    private static final String CHANGE_THEME_BUTTON = "changeTheme";

    private FrameFixture        chatWindow;

    private User                user                = new User("Chatter");
    private User                user2               = new User("Second");
    private Chat                chat                = new Chat().addUser(user).addUser(user2);

    @BeforeClass
    public static void mock()
        throws Exception
    {
        mockStatic(ImageIO.class);
        doThrow(new IOException()).when(ImageIO.class, "read", any());
    }

    @Before
    @BeforeEach
    public void setup()
    {
        ChatWindow component = GuiActionRunner.execute(() -> new ChatWindow(user, chat));
        chatWindow = new FrameFixture(component);
        chatWindow.show();
    }

    @After
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
        chatWindow.textBox(CHAT_INPUT).enterText("Test message").pressAndReleaseKeys(VK_ENTER);

        JListFixture listFixture = chatWindow.list(MESSAGES_LIST);
        listFixture.requireItemCount(1);
        JList<String> messageList = listFixture.target();
        assertThat(messageList.getModel().getElementAt(0), containsString("Test message"));
        chatWindow.textBox(CHAT_INPUT).requireText("");
    }

    @Test
    public void should_not_add_empty_message_to_list()
    {
        chatWindow.textBox(CHAT_INPUT).pressAndReleaseKeys(VK_ENTER);

        chatWindow.list(MESSAGES_LIST).requireItemCount(0);
        assertThat(user.getMessages(), hasSize(0));
    }

    @Test
    public void should_not_add_message_to_list_for_deselected_user()
    {
        chatWindow.checkBox(user.getName()).uncheck();

        chatWindow.textBox(CHAT_INPUT).enterText("Test message").pressAndReleaseKeys(VK_ENTER);

        chatWindow.list(MESSAGES_LIST).requireItemCount(0);
    }

    @Test
    public void should_create_group()
    {
        chatWindow.button(NEW_GROUP_BUTTON).click();

        chatWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);

        assertThat(chat.getGroups(), hasSize(1));
        chatWindow.toggleButton("Groupie").requireVisible().requireText("Groupie").requireToolTip("Chatter, Second");
    }

    @Test
    public void should_not_create_group_without_users()
    {
        chatWindow.checkBox(user.getName()).uncheck();
        chatWindow.checkBox(user2.getName()).uncheck();

        chatWindow.button(NEW_GROUP_BUTTON).click();

        assertThat(chat.getGroups(), hasSize(0));
        chatWindow.dialog().label(withText("Group cannot be empty")).requireVisible();
    }

    @Test
    public void should_not_create_group_without_name()
    {
        chatWindow.button(NEW_GROUP_BUTTON).click();

        chatWindow.dialog().button(JButtonMatcher.withText("OK")).click();

        assertThat(chat.getGroups(), hasSize(0));
        chatWindow.dialog().label(withText("Group name cannot be empty")).requireVisible();
    }

    @Test
    public void should_not_create_group_with_null_name()
    {
        chatWindow.button(NEW_GROUP_BUTTON).click();

        chatWindow.dialog().close();

        assertThat(chat.getGroups(), hasSize(0));
    }

    @Test
    public void should_not_create_group_with_same_name_and_members()
    {
        chat.addGroup(new Group("Groupie", chat.getUsers()));

        chatWindow.button(NEW_GROUP_BUTTON).click();
        chatWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);

        assertThat(chat.getGroups(), hasSize(1));
        chatWindow.dialog().label(withText("Same group already exists")).requireVisible();
    }

    @Test
    public void should_remove_group_on_close()
    {
        chatWindow.checkBox(user2.getName()).click();
        chatWindow.button(NEW_GROUP_BUTTON).click();
        chatWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);
        assertThat(chat.getGroups(), hasSize(1));

        chatWindow.close();

        assertThat(chat.getGroups(), hasSize(0));
    }

    @Test
    public void should_not_remove_group_on_close_if_user_not_in_it()
    {
        chat.addGroup(new Group("Groupie", user2));

        chatWindow.close();

        assertThat(chat.getGroups(), hasSize(1));
    }

    @Test
    public void should_select_users_in_group()
    {
        chat.addGroup(new Group("Groupie", user));

        JCheckBoxFixture chatterCheckBox = chatWindow.checkBox(user.getName()).click();
        chatWindow.toggleButton("Groupie").click();

        chatterCheckBox.requireSelected();
        chatWindow.checkBox(user2.getName()).requireNotSelected();
    }

    @Test
    public void should_change_to_darkmode()
    {
        chatWindow.button(CHANGE_THEME_BUTTON).click();
        assertThat(chatWindow.list(MESSAGES_LIST).background().target(), is(ThemedColors.darkSecondary));
    }

    @Test
    public void should_have_button_icon()
    {
        assertThat(chatWindow.button(NEW_GROUP_BUTTON).target().getIcon(), notNullValue());
    }

    @org.junit.Test
    public void should_have_button_label_when_image_cannot_be_loaded()
        throws IOException
    {
        assertThat(chatWindow.button(NEW_GROUP_BUTTON).target().getText(), is("New group"));
    }
}
