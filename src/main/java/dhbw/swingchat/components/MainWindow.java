package dhbw.swingchat.components;

import static dhbw.swingchat.helper.MessageUtil.showError;
import static dhbw.swingchat.helper.MessageUtil.showWarning;
import static dhbw.swingchat.storage.Storage.loadChat;
import static dhbw.swingchat.storage.Storage.storeChat;
import static java.awt.event.WindowEvent.WINDOW_CLOSING;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
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

    private transient Chat    chat             = new Chat();

    /**
     * Build the main window of the chat application.
     */
    public MainWindow()
    {
        Chat loadedChat = loadChat();
        if (loadedChat != null) {
            chat = loadedChat;
            for (User user : chat.getUsers()) {
                chatWindows.add(new ChatWindow(user, chat));
            }
        }
        add(new JLabel("Username"));
        addNameInput();
        addButton();
        setSize(220, 125);
        setLayout(new GridLayout(3, 1));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e)
            {
                try {
                    storeChat(chat);
                } catch (IOException exception) {
                    showError(rootPane, "Couldn't store application state");
                }
                for (ChatWindow chatWindow : chatWindows) {
                    chatWindow.dispatchEvent(new WindowEvent(chatWindow, WINDOW_CLOSING));
                }
                super.windowClosing(e);
            }
        });
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
        button.setText("Login");
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
                if (chat.getUser(name) == null) {
                    User user = new User(name);
                    chat.addUser(user);
                    chatWindows.add(new ChatWindow(user, chat));
                    username.setText("");
                }
                else {
                    showWarning(username, "Username already exists");
                }
            }
            else {
                showWarning(username, "Username cannot be empty");
            }
        }
    }

    public Chat getChat()
    {
        return chat;
    }
}
