package dhbw.swingchat.test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtil
{

    private TestUtil()
    {
        // utility class
    }

    /**
     * Get a field value of any access from an object.
     */
    public static Object getFieldValue(Object target, String field)
    {
        Class<?> c = target.getClass();
        try {
            Field f = getFieldFromHierarchy(c, field);
            f.setAccessible(true);
            return f.get(target);
        } catch (Exception e) {
            throw new RuntimeException("Unable to set internal state on a private field.", e);
        }
    }

    private static Field getFieldFromHierarchy(Class<?> clazz, String field)
    {
        Field f = getField(clazz, field);
        while (f == null && clazz != Object.class) {
            clazz = clazz.getSuperclass();
            f = getField(clazz, field);
        }
        if (f == null) {
            throw new RuntimeException(
                    "You want me to get this field: '" + field +
                            "' on this class: '" + clazz.getSimpleName() +
                            "' but this field is not declared within the hierarchy of this class!");
        }
        return f;
    }

    private static Field getField(Class<?> clazz, String field)
    {
        try {
            return clazz.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * Delete chat file.
     */
    public static void clearStorage()
    {
        try {
            Files.deleteIfExists(Paths.get("Chat.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
