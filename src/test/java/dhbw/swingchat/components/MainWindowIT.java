package dhbw.swingchat.components;

import static dhbw.swingchat.test.TestUtil.getFieldValue;
import static java.awt.event.KeyEvent.VK_ENTER;
import static org.assertj.swing.core.matcher.JLabelMatcher.withText;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.swing.JFrame;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.exception.WaitTimedOutError;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dhbw.swingchat.instance.Group;
import dhbw.swingchat.instance.User;
import dhbw.swingchat.test.TestUtil;

/**
 * UI test for the {@link MainWindow}.
 */
public class MainWindowIT
{

    private MainWindow   component;
    private FrameFixture mainWindow;

    @BeforeEach
    public void setup()
    {
        component = GuiActionRunner.execute(() -> new MainWindow());
        component.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        assertTrue(mainWindow.textBox("username").target().isVisible());
    }

    @Test
    public void should_have_button()
    {
        assertTrue(mainWindow.button("login").target().isEnabled());
    }

    @Test
    public void should_have_button_label()
    {
        assertThat(mainWindow.button("login").target().getText(), is("Login"));
    }

    @Test
    public void should_not_open_chat_window_for_empty_name()
    {
        mainWindow.button("login").click();

        assertThat(component.getChat().getUsers(), hasSize(0));
        mainWindow.dialog().label(withText("Username cannot be empty")).requireVisible();
        Assertions.assertThrows(WaitTimedOutError.class, () -> {
            WindowFinder.findFrame(ChatWindow.class).withTimeout(100).using(mainWindow.robot());
        });
    }

    @Test
    public void should_not_create_chat_window_for_existing_name()
    {
        mainWindow.textBox("username").enterText("testName");
        mainWindow.button("login").click();

        mainWindow.textBox("username").enterText("testName");
        mainWindow.button("login").click();

        assertThat(component.getChat().getUsers(), hasSize(1));
        mainWindow.dialog().label(withText("Username already exists")).requireVisible();
        WindowFinder.findFrame("testName").using(mainWindow.robot()).requireVisible();
    }

    @Test
    public void should_create_chat_window_for_new_user()
    {
        mainWindow.textBox("username").enterText("testName");

        mainWindow.button("login").click();

        assertThat(component.getChat().getUsers(), hasSize(1));
        WindowFinder.findFrame("testName").using(mainWindow.robot()).requireVisible().requireTitle("testName");
    }

    @Test
    public void should_create_new_user_with_enter_key()
    {
        mainWindow.textBox("username").enterText("testName").pressAndReleaseKeys(VK_ENTER);

        assertThat(component.getChat().getUsers(), hasSize(1));
        WindowFinder.findFrame("testName").using(mainWindow.robot()).requireVisible();
    }

    @Test
    public void should_update_chat_window()
    {
        mainWindow.textBox("username").enterText("testName1").pressAndReleaseKeys(VK_ENTER);

        mainWindow.textBox("username").enterText("testName2").pressAndReleaseKeys(VK_ENTER);

        WindowFinder.findFrame("testName1").using(mainWindow.robot()).checkBox("testName2").requireVisible();
    }

    @Test
    public void should_add_user_to_group()
    {
        mainWindow.textBox("username").enterText("testName1").pressAndReleaseKeys(VK_ENTER);
        mainWindow.textBox("username").enterText("testName2").pressAndReleaseKeys(VK_ENTER);

        WindowFinder.findFrame("testName1").using(mainWindow.robot()).button("newGroup").click();

        mainWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);

        assertTrue(WindowFinder.findFrame("testName2").using(mainWindow.robot()).button("Groupie").target().isVisible());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_delete_user_from_group()
    {
        mainWindow.textBox("username").enterText("testName1").pressAndReleaseKeys(VK_ENTER);
        mainWindow.textBox("username").enterText("testName2").pressAndReleaseKeys(VK_ENTER);

        WindowFinder.findFrame("testName1").using(mainWindow.robot()).button("newGroup").click();

        mainWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);

        WindowFinder.findFrame("testName2").using(mainWindow.robot()).close();
        Group groupie = component.getChat().getGroups().get(0);
        List<User> users = (List<User>)getFieldValue(groupie, "users");
        assertThat(users, hasSize(1));
    }
}
