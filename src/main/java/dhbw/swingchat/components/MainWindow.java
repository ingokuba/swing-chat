package dhbw.swingchat.components;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dhbw.swingchat.instance.Chat;
import dhbw.swingchat.instance.User;

/**
 * Starting screen of the application. Contains an input field and a button to register users.
 */
public class MainWindow extends JFrame
{

    private static final long serialVersionUID = 1L;

    private JTextField        username;
    private List<ChatWindow>  chatWindows      = new ArrayList<>();

    Chat                      chat             = new Chat();

    public MainWindow()
    {
        add(new JLabel("Username"));
        addNameInput();
        addButton();
        setSize(220, 125);
        setLayout(new GridLayout(3, 1));
        setVisible(true);
        // TODO close all open chat windows
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Creates the input field for the username.
     */
    private void addNameInput()
    {
        username = new JTextField();
        username.setName("username");
        username.setSize(200, 30);
        username.setAction(new CreateUserAction());
        add(username);
    }

    /**
     * Creates the button to create a new user.
     */
    private void addButton()
    {
        JButton button = new JButton();
        button.setSize(200, 30);
        button.setName("login");
        button.setAction(new CreateUserAction());
        add(button);
    }

    /**
     * Creates a new user with the entered name if it doesn't already exist.
     */
    private class CreateUserAction extends AbstractAction
    {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            String name = username.getText();
            if (!name.isEmpty()) {
                if (chat.absent(name)) {
                    User user = new User(name);
                    chat.add(user);
                    chatWindows.add(new ChatWindow(user, chat));
                    username.setText("");
                }
            }
        }
    }

    public Chat getChat()
    {
        return chat;
    }
}
