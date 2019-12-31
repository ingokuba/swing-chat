package dhbw.swingchat.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Container;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import dhbw.swingchat.helper.ThemedColors;
import dhbw.swingchat.helper.ThemedJFrame;

public class DarkJFrameIT
{

    private ThemedJFrame frame;
    private JButton testButton;

    @Before
    @BeforeEach
    public void setupFrame() {
        frame = new ThemedJFrame();
    }

    @Test
    public void sould_be_light_mode(){
        assertThat("Init with light-mode", frame.getDarkModeBoolean(), is(Boolean.FALSE));
        assertThat(frame.getForegroundColor(), is(ThemedColors.lightForeground));
    }

    public void populate_frame(){
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

    @Test
    public void should_switch_mode(){
        frame.toggleTheme();
        assertThat("switched to dark-mode", frame.getDarkModeBoolean(), is(Boolean.TRUE));
        assertThat(frame.getBackgroundColor(), is(ThemedColors.darkBackround));

        frame.toggleTheme();
        assertThat("switch back to light-mode again", frame.getDarkModeBoolean(), is(Boolean.FALSE));
    }

    @Test
    public void validate_frame_changed(){
        populate_frame();
        frame.toggleTheme();

        assertThat("test frame is dark", frame.getBackground(), is(frame.getBackgroundColor()));
    }

    @Test
    public void validate_test_button_changed(){
        populate_frame();
        frame.toggleTheme();

        assertThat("test button is dark", testButton.getBackground(), is(frame.getSecondaryColor()));
    }

    @Test
    public void should_update_frame(){
        populate_frame();

        assertDoesNotThrow(() -> frame.updateFrame());
    }
}

