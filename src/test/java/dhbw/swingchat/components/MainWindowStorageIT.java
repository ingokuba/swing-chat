package dhbw.swingchat.components;

import static dhbw.swingchat.storage.Storage.loadChat;
import static dhbw.swingchat.storage.Storage.storeChat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import javax.swing.JFrame;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dhbw.swingchat.instance.Chat;
import dhbw.swingchat.instance.Group;
import dhbw.swingchat.instance.User;

public class MainWindowStorageIT
{

    private FrameFixture mainWindow;

    @BeforeEach
    public void setup()
    {
        User user = new User("Tester").message("Hello");
        Group group = new Group("Group", user);
        Chat chat = new Chat().addUser(user).addGroup(group);
        storeChat(chat);
        MainWindow component = GuiActionRunner.execute(() -> new MainWindow());
        component.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainWindow = new FrameFixture(component);
        mainWindow.show();
    }

    @AfterEach
    private void cleanup()
    {
        mainWindow.cleanUp();
    }

    @Test
    public void should_load_chat()
    {
        FrameFixture chatWindow = WindowFinder.findFrame("Tester").using(mainWindow.robot());

        chatWindow.checkBox("Tester").requireSelected();
        chatWindow.button("Group").requireVisible();
        assertThat(chatWindow.list().contents(), arrayContaining("Hello"));
    }

    @Test
    public void should_store_chat_on_close()
    {
        mainWindow.close();

        Chat chat = loadChat();

        assertThat(chat, notNullValue());
        User user = chat.getUser("Tester");
        assertThat(user, notNullValue());
        assertThat(user.getMessages(), hasItem("Hello"));
        assertThat(chat.getGroups(), hasSize(1));
        Group group = chat.getGroups().get(0);
        assertThat(group, notNullValue());
        assertThat(group.getName(), is("Group"));
        assertTrue(group.contains(user));
    }
}
