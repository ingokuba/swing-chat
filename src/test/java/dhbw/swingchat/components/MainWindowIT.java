package dhbw.swingchat.components;

import static dhbw.swingchat.components.ChatWindowIT.NEW_GROUP_BUTTON;
import static dhbw.swingchat.storage.Storage.storeChat;
import static dhbw.swingchat.test.TestUtil.SKIP_BEFORE_EACH;
import static dhbw.swingchat.test.TestUtil.getFieldValue;
import static java.awt.event.KeyEvent.VK_ENTER;
import static org.assertj.swing.core.matcher.JLabelMatcher.withText;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.exception.WaitTimedOutError;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import dhbw.swingchat.instance.Chat;
import dhbw.swingchat.instance.Group;
import dhbw.swingchat.instance.User;
import dhbw.swingchat.test.TestUtil;

/**
 * UI test for the {@link MainWindow}.
 */
public class MainWindowIT
{

    private static final String USERNAME_INPUT = "username";
    private static final String LOGIN_BUTTON   = "login";

    private FrameFixture        mainWindow;
    private Chat                chat           = new Chat();

    @BeforeEach
    public void setup(TestInfo testInfo)
    {
        if (testInfo != null && testInfo.getTags().contains(SKIP_BEFORE_EACH)) {
            return;
        }
        MainWindow component = GuiActionRunner.execute(() -> new MainWindow());
        component.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chat = component.getChat();
        mainWindow = new FrameFixture(component);
        mainWindow.show();
    }

    @AfterEach
    private void cleanup()
    {
        mainWindow.cleanUp();
        TestUtil.clearStorage();
    }

    @Test
    public void should_close_window()
    {
        assertTrue(mainWindow.target().isVisible());

        mainWindow.close();

        assertFalse(mainWindow.target().isVisible());
    }

    @Test
    public void should_have_inputfield()
    {
        assertTrue(mainWindow.textBox(USERNAME_INPUT).target().isVisible());
    }

    @Test
    public void should_have_button()
    {
        assertTrue(mainWindow.button(LOGIN_BUTTON).target().isEnabled());
    }

    @Test
    public void should_have_button_label()
    {
        assertThat(mainWindow.button(LOGIN_BUTTON).target().getText(), is("Login"));
    }

    @Test
    public void should_not_open_chat_window_for_empty_name()
    {
        mainWindow.button(LOGIN_BUTTON).click();

        assertThat(chat.getUsers(), hasSize(0));
        mainWindow.dialog().label(withText("Username cannot be empty")).requireVisible();
        Assertions.assertThrows(WaitTimedOutError.class, () -> {
            WindowFinder.findFrame(ChatWindow.class).withTimeout(100).using(mainWindow.robot());
        });
    }

    @Test
    @Tag(SKIP_BEFORE_EACH)
    public void should_not_create_chat_window_for_existing_name()
        throws IOException
    {
        User user = new User("testName");
        chat.addUser(user);
        storeChat(chat);
        setup(null);

        mainWindow.textBox(USERNAME_INPUT).enterText(user.getName());
        mainWindow.button(LOGIN_BUTTON).click();

        assertThat(chat.getUsers(), hasSize(1));
        mainWindow.dialog().label(withText("Username already exists")).requireVisible();
        WindowFinder.findFrame(user.getName()).using(mainWindow.robot()).requireVisible();
    }

    @Test
    public void should_create_chat_window_for_new_user()
    {
        mainWindow.textBox(USERNAME_INPUT).enterText("testName");

        mainWindow.button(LOGIN_BUTTON).click();

        assertThat(chat.getUsers(), hasSize(1));
        WindowFinder.findFrame("testName").using(mainWindow.robot()).requireVisible().requireTitle("testName");
    }

    @Test
    public void should_create_new_user_with_enter_key()
    {
        mainWindow.textBox(USERNAME_INPUT).enterText("testName").pressAndReleaseKeys(VK_ENTER);

        assertThat(chat.getUsers(), hasSize(1));
        WindowFinder.findFrame("testName").using(mainWindow.robot()).requireVisible();
    }

    @Test
    public void should_update_chat_window()
    {
        mainWindow.textBox(USERNAME_INPUT).enterText("testName1").pressAndReleaseKeys(VK_ENTER);

        mainWindow.textBox(USERNAME_INPUT).enterText("testName2").pressAndReleaseKeys(VK_ENTER);

        WindowFinder.findFrame("testName1").using(mainWindow.robot()).checkBox("testName2").requireVisible();
    }

    @Test
    @Tag(SKIP_BEFORE_EACH)
    public void should_add_user_to_group()
        throws IOException
    {
        User user1 = new User("testName1");
        User user2 = new User("testName2");
        chat.addUser(user1).addUser(user2);
        storeChat(chat);
        setup(null);

        WindowFinder.findFrame(user1.getName()).using(mainWindow.robot()).button(NEW_GROUP_BUTTON).click();

        mainWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);

        assertTrue(WindowFinder.findFrame(user2.getName()).using(mainWindow.robot()).toggleButton("Groupie").target().isVisible());
    }

    @Test
    @Tag(SKIP_BEFORE_EACH)
    @SuppressWarnings("unchecked")
    public void should_delete_user_from_group()
        throws IOException
    {
        User user1 = new User("testName1");
        User user2 = new User("testName2");
        chat.addUser(user1).addUser(user2).addGroup(new Group("Groupie", user1, user2));
        storeChat(chat);
        setup(null);

        WindowFinder.findFrame(user2.getName()).using(mainWindow.robot()).close();
        Group groupie = chat.getGroups().get(0);
        List<User> users = (List<User>)getFieldValue(groupie, "users");
        assertThat(users, hasSize(1));
    }

    @Test
    @Tag(SKIP_BEFORE_EACH)
    public void should_delete_group_from_chat()
        throws IOException
    {
        // prepare chat instance
        User user1 = new User("testName1");
        User user2 = new User("testName2");
        chat.addUser(user1).addUser(user2);
        chat.addGroup(new Group("Group1", user1));
        chat.addGroup(new Group("All", user1, user2));
        storeChat(chat);
        setup(null);

        // close window of user 1
        WindowFinder.findFrame(user1.getName()).using(mainWindow.robot()).close();

        // group 'All' still exists
        assertThat(chat.getGroups(), hasSize(1));
    }
}
