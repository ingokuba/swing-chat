package dhbw.swingchat.components;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainWindowIT
{

    private FrameFixture mainWindow;

    @BeforeEach
    public void setup()
    {
        MainWindow frame = GuiActionRunner.execute(() -> new MainWindow());
        mainWindow = new FrameFixture(frame);
        mainWindow.show();
    }

    @AfterEach
    private void cleanup()
    {
        mainWindow.cleanUp();
    }

    @Test
    public void should_close_window()
    {
        mainWindow.requireVisible();

        mainWindow.close();

        mainWindow.requireNotVisible();
    }
}
