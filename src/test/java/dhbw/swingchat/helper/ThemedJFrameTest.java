package dhbw.swingchat.helper;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.awt.Button;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for abstract class {@link ThemedJFrame}.
 */
public class ThemedJFrameTest
{

    private ThemedJFrame frame;
    private JButton      testButton;

    @BeforeEach
    public void setupFrame()
    {
        frame = new TestFrame();
    }

    @Test
    public void sould_default_to_light_mode()
    {
        assertThat(frame.getForegroundColor(), is(ThemedColors.lightForeground));
    }

    @Test
    public void should_switch_mode()
    {
        frame.toggleTheme();
        assertThat(frame.getBackgroundColor(), is(ThemedColors.darkBackround));

        frame.toggleTheme();
        assertThat(frame.getBackgroundColor(), is(ThemedColors.lightBackround));
    }

    @Test
    public void validate_frame_changed()
    {
        populate_frame();
        frame.toggleTheme();

        assertThat("test frame is dark", frame.getBackground(), is(frame.getBackgroundColor()));
    }

    @Test
    public void validate_test_button_changed()
    {
        populate_frame();
        frame.toggleTheme();

        assertThat("test button is dark", testButton.getBackground(), is(frame.getSecondaryColor()));
    }

    @Test
    public void should_update_frame()
    {
        populate_frame();

        assertDoesNotThrow(() -> frame.updateFrame());
    }

    @Test
    public void should_update_frame_for_component_thats_not_container()
    {
        frame.add(new Button());
        populate_frame();

        assertDoesNotThrow(() -> frame.updateFrame());
    }

    private void populate_frame()
    {
        frame.add(new JButton());
        frame.add(new JCheckBox());
        frame.add(new JTextField());

        Container superContainer = new Container();
        JPanel subPanel = new JPanel();
        this.testButton = new JButton();

        subPanel.add(testButton);
        superContainer.add(subPanel);
        frame.add(superContainer);
    }

    private class TestFrame extends ThemedJFrame
    {

        private static final long serialVersionUID = 1L;
    }
}
