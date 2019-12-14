package dhbw.swingchat.storage;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;

import dhbw.swingchat.instance.Chat;
import dhbw.swingchat.instance.User;

public class Storage
{

    private static final Path PATH = Paths.get("Chat.json");

    private Storage()
    {
        // utility class
    }

    /**
     * Store chat in a file.
     */
    public static final boolean storeChat(Chat chat)
    {
        deleteObservers(chat);
        String json = new Gson().toJson(chat);
        try {
            Files.write(PATH, json.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Load chat instance from file system.
     */
    public static final Chat loadChat()
    {
        try (BufferedReader reader = Files.newBufferedReader(PATH, UTF_8)) {
            return new Gson().fromJson(reader, Chat.class);
        } catch (IOException e) {
            return null;
        }
    }

    private static void deleteObservers(Chat chat)
    {
        chat.deleteObservers();
        chat.getUsers().forEach(User::deleteObservers);
    }
}
