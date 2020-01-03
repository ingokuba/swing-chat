package dhbw.swingchat.storage;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dhbw.swingchat.instance.Chat;

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
    public static final void storeChat(Chat chat)
        throws IOException
    {
        String json = gson().toJson(chat);
        Files.write(PATH, json.getBytes());
    }

    /**
     * Load chat instance from file system.
     */
    public static final Chat loadChat()
    {
        try (BufferedReader reader = Files.newBufferedReader(PATH, UTF_8)) {
            return gson().fromJson(reader, Chat.class);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Searches an image with a given name.
     * 
     * @param imageName Name of the image to look for - '.png' is appended.
     * @return The found image or null.
     */
    public static Image getPngImage(String imageName)
    {
        try {
            return ImageIO.read(Storage.class.getResource("/img/" + imageName + ".png"));
        } catch (Exception ex) {
            return null;
        }
    }

    private static Gson gson()
    {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }
}
