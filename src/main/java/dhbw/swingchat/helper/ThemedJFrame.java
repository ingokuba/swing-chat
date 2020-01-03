package dhbw.swingchat.helper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * Frame that can change between dark and light mode.
 */
public abstract class ThemedJFrame extends JFrame
{

    private static final long serialVersionUID = 1L;

    private boolean           isDarkMode;

    public ThemedJFrame()
    {
        this.isDarkMode = false;
    }

    public boolean getDarkModeBoolean()
    {
        return isDarkMode;
    }

    /**
     * Get all components recursively and change the fore and background color.
     */
    private void setAllCompontentsRecrusively(Color fg, Color bg, Color buttonBg, Component[] components)
    {
        for (Component component : components) {

            Color newBg = bg;

            if (component instanceof Container) {
                setAllCompontentsRecrusively(fg, bg, buttonBg, ((Container)component).getComponents());
            }
            if (component instanceof JButton || component instanceof JTextField) {
                newBg = buttonBg;
            }
            component.setBackground(newBg);
            component.setForeground(fg);
        }
    }

    public Color getForegroundColor()
    {
        return isDarkMode ? ThemedColors.darkForeground : ThemedColors.lightForeground;
    }

    public Color getBackgroundColor()
    {
        return isDarkMode ? ThemedColors.darkBackround : ThemedColors.lightBackround;
    }

    public Color getSecondaryColor()
    {
        return isDarkMode ? ThemedColors.darkSecondary : ThemedColors.lightSecondary;
    }

    /**
     * Update colors of the frame depending on the mode.
     */
    public void updateFrame()
    {
        Color bg = getBackgroundColor();
        Color fg = getForegroundColor();
        Color secBgColor = getSecondaryColor();

        setBackground(bg);
        setForeground(fg);

        setAllCompontentsRecrusively(fg, bg, secBgColor, this.getComponents());
    }

    /**
     * change dark / light mode
     */
    public void toggleTheme()
    {
        isDarkMode = !isDarkMode;
        updateFrame();
    }
}