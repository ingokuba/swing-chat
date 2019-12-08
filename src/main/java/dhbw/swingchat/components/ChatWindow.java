package dhbw.swingchat.components;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dhbw.swingchat.instance.Chat;
import dhbw.swingchat.instance.User;

/**
 * Chat window for a specific user.
 */
public class ChatWindow extends JFrame
    implements Observer
{

    private static final long serialVersionUID = 1L;

    private User              user;
    private Chat              chat;

    private JPanel            userPanel;

    public ChatWindow(User user, Chat chat)
    {
        this.user = user;
        this.chat = chat;
        this.chat.addObserver(this);
        userPanel = new JPanel();
        updateUsers();
        add(userPanel);
        setName(user.getName());
        setSize(500, 500);
        setLayout(new GridLayout(3, 1));
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e)
            {
                chat.remove(user);
            }
        });
    }

    private void updateUsers()
    {
        chat.getUsers().forEach((name, user) -> {
            JCheckBox box = new JCheckBox(name, true);
            box.setName(name);
            userPanel.add(box);
        });
    }

    /**
     * Message all selected users with the entered message.
     * Prefixes the message with the username of this window.
     */
    public void message(String message)
    {
        chat.message(user.getName() + ": " + message, getSelectedUsers());
    }

    /**
     * Return all selected users from the check boxes.
     */
    private String[] getSelectedUsers()
    {
        List<String> names = new ArrayList<>();
        for (Component component : userPanel.getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox box = (JCheckBox)component;
                if (box.isSelected()) {
                    names.add(box.getText());
                }
            }
        }
        return names.toArray(new String[0]);
    }

    @Override
    public void update(Observable observableChat, Object arg1)
    {
        this.chat = (Chat)observableChat;
        userPanel.setVisible(false);
        userPanel.removeAll();
        updateUsers();
        userPanel.setVisible(true);
    }
}
