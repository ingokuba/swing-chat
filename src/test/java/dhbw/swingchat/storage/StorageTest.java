package dhbw.swingchat.storage;

import static dhbw.swingchat.storage.Storage.loadChat;
import static dhbw.swingchat.storage.Storage.storeChat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import dhbw.swingchat.instance.Chat;
import dhbw.swingchat.instance.Group;
import dhbw.swingchat.instance.User;
import dhbw.swingchat.test.TestUtil;

public class StorageTest
{

    @BeforeAll
    public static void clearStorage()
    {
        TestUtil.clearStorage();
    }

    @Test
    @Order(0)
    public void should_return_null_if_file_doesnt_exist()
    {
        assertThat(loadChat(), nullValue());
    }

    @Test
    @Order(1)
    public void should_store_chat()
    {
        User user = new User("Tester");
        user.message("Test message");
        Group group = new Group("Group", user);
        Chat chat = new Chat();
        chat.addUser(user);
        chat.addGroup(group);

        assertDoesNotThrow(() -> storeChat(chat));
    }

    @Test
    @Order(2)
    public void should_load_chat()
    {
        Chat chat = loadChat();

        User user = chat.getUser("Tester");
        assertThat(user, notNullValue());
        assertThat(user.getMessages(), hasSize(1));
        assertThat(chat.getGroups(), contains(hasProperty("name", is("Group"))));
    }
}
