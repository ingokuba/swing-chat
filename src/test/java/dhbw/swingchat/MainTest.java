package dhbw.swingchat;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

/**
 * Test for the {@link Main} class.
 */
public class MainTest
{

    @Test
    public void should_not_throw_exception()
    {
        assertDoesNotThrow(() -> Main.main(null));
    }
}
