package dhbw.swingchat.components;

import static dhbw.swingchat.test.TestUtil.getFieldValue;
import static java.awt.event.KeyEvent.VK_ENTER;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.exception.WaitTimedOutError;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import dhbw.swingchat.instance.Group;
import dhbw.swingchat.instance.User;

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
        mainWindow = new FrameFixture(component);
        mainWindow.show();
    }

    @AfterEach
    private void cleanup()
    {
        mainWindow.cleanUp();
    }

    @Test
    @Disabled("Hard to test, because System.exit is called")
    public void should_close_window()
    {
        mainWindow.requireVisible();

        mainWindow.close();

        mainWindow.requireNotVisible();
    }

    @Test
    public void should_have_inputfield()
    {
        mainWindow.textBox("username").requireVisible();
    }

    @Test
    public void should_have_button()
    {
        mainWindow.button("login").requireEnabled();
    }

    @Test
    public void should_not_open_chat_window_for_empty_name()
    {
        mainWindow.button("login").click();

        assertThat(component.getChat().getUsers(), anEmptyMap());
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

        assertThat(component.getChat().getUsers(), aMapWithSize(1));
        WindowFinder.findFrame("testName").using(mainWindow.robot()).requireVisible();
    }

    @Test
    public void should_create_chat_window_for_new_user()
    {
        mainWindow.textBox("username").enterText("testName");

        mainWindow.button("login").click();

        assertThat(component.getChat().getUsers(), aMapWithSize(1));
        WindowFinder.findFrame("testName").using(mainWindow.robot()).requireVisible();
    }

    @Test
    public void should_create_new_user_with_enter_key()
    {
        mainWindow.textBox("username").enterText("testName").pressAndReleaseKeys(VK_ENTER);

        assertThat(component.getChat().getUsers(), aMapWithSize(1));
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

        WindowFinder.findFrame("testName1").using(mainWindow.robot()).button().click();

        mainWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);

        WindowFinder.findFrame("testName2").using(mainWindow.robot()).radioButton().requireVisible();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_delete_user_from_group()
    {
        mainWindow.textBox("username").enterText("testName1").pressAndReleaseKeys(VK_ENTER);
        mainWindow.textBox("username").enterText("testName2").pressAndReleaseKeys(VK_ENTER);

        WindowFinder.findFrame("testName1").using(mainWindow.robot()).button().click();

        mainWindow.dialog().textBox().enterText("Groupie").pressAndReleaseKeys(VK_ENTER);

        WindowFinder.findFrame("testName2").using(mainWindow.robot()).close();
        Group groupie = component.getChat().getGroups().get(0);
        List<User> users = (List<User>)getFieldValue(groupie, "users");
        assertThat(users, hasSize(1));
    }
}
