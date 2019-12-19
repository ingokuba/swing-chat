package dhbw.swingchat.helper;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Container;
import java.awt.Component;

public class ThemedJFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;

    public ThemedJFrame() {
        this.isDarkMode = false;
    }

    private boolean isDarkMode;

    public Boolean getDarkModeBoolean() {
        return isDarkMode;
    }

    // get all components recrusively and change the fore and background color
    private void setAllCompontentsRecrusively(Color fg, Color bg, Color buttonBg, Component[] components) {
        for (Component component : components) {

            Color newBg = bg;

            if (component instanceof Container) {
                setAllCompontentsRecrusively(fg, bg, buttonBg, ((Container) component).getComponents());
            }
            if (component instanceof JButton || component instanceof JTextField) {
                newBg = buttonBg;
            }
            component.setBackground(newBg);
            component.setForeground(fg);
        }
    }

    public Color getForegroundColor(){
        return this.isDarkMode ? ThemedColors.darkForeground : ThemedColors.lightForeground;
    }

    public Color getBackgroundColor(){
        return this.isDarkMode ? ThemedColors.darkBackround : ThemedColors.lightBackround;
    }

    public Color getSecondaryColor(){
        return this.isDarkMode ? ThemedColors.darkSecondary : ThemedColors.lightSecondary;   
    }

    public void updateFrame() {
        Color bg = getBackgroundColor();
        Color fg = getForegroundColor();
        Color secBgColor = getSecondaryColor();

        setBackground(bg);
        setForeground(fg);
        
        setAllCompontentsRecrusively(fg, bg, secBgColor, this.getComponents());
    }

    // change dark / light mode
    public void toggleTheme() {
        this.isDarkMode = !this.isDarkMode;
        updateFrame();
    }
}