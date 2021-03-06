package dhbw.swingchat.helper;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Component;

public class MessageUtil
{

    private MessageUtil()
    {
        // utility class
    }

    /**
     * Show warning message dialog.
     */
    public static void showWarning(Component parent, String message)
    {
        showMessageDialog(parent, message, "Warning", WARNING_MESSAGE);
    }

    /**
     * Show error message dialog.
     */
    public static void showError(Component parent, String message)
    {
        showMessageDialog(parent, message, "Error", ERROR_MESSAGE);
    }
}
