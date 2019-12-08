package dhbw.swingchat.components;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dhbw.swingchat.instance.Chat;
import dhbw.swingchat.instance.User;

/**
 * Chat window for a specific user.
 */
public class ChatWindow extends JFrame
{

    private static final long serialVersionUID = 1L;

    private User              user;
    private Chat              chat;

    private JPanel            userPanel;
    private JList<String>     messageList;

    public ChatWindow(User user, Chat chat)
    {
        this.user = user;
        this.user.addObserver(new UserObserver());
        this.chat = chat;
        this.chat.addObserver(new ChatObserver());
        userPanel = new JPanel();
        updateUsers();
        add(userPanel);
        messageList = new JList<>();
        updateMessageList();
        messageList.setName("messages");
        messageList.setSize(500, 1000);
        add(messageList);
        addUserInput();
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

    private void updateMessageList()
    {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String message : user.getMessages()) {
            model.addElement(message);
        }
        messageList.setModel(model);
    }

    private void addUserInput()
    {
        JTextField userInput = new JTextField();
        userInput.setName("userInput");
        userInput.setSize(200, 30);
        userInput.setAction(new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                String message = userInput.getText();
                if (!message.isEmpty()) {
                    message(message);
                }
            }
        });
        add(userInput);
    }

    /**
     * Message all selected users with the entered message.
     * Prefixes the message with the username of this window.
     */
    private void message(String message)
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

    private class ChatObserver
        implements Observer
    {

        @Override
        public void update(Observable observableChat, Object arg1)
        {
            userPanel.setVisible(false);
            userPanel.removeAll();
            updateUsers();
            userPanel.setVisible(true);
        }
    }

    private class UserObserver
        implements Observer
    {

        @Override
        public void update(Observable observableUser, Object arg1)
        {
            updateMessageList();
        }
    }
}
