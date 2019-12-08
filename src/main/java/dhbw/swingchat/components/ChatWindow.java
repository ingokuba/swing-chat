package dhbw.swingchat.components;

import static javax.swing.JOptionPane.showInputDialog;

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
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import dhbw.swingchat.ChangeMode;
import dhbw.swingchat.instance.Chat;
import dhbw.swingchat.instance.Group;
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
    private JPanel            groupPanel;
    private JList<String>     messageList;

    public ChatWindow(User user, Chat chat)
    {
        this.user = user;
        this.user.addObserver(new ChatObserver());
        this.chat = chat;
        this.chat.addObserver(new ChatObserver());
        userPanel = new JPanel();
        updateUsers();
        add(userPanel);
        JButton addGroup = new JButton("New group");
        addGroup.setAction(new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                List<User> selectedUsers = getSelectedUsers();
                if (selectedUsers.isEmpty()) {
                    return;
                }
                String groupName = showInputDialog("Enter group name");
                if (groupName == null || groupName.isEmpty()) {
                    return;
                }
                chat.addGroup(new Group(groupName, selectedUsers));
            }
        });
        add(addGroup);
        groupPanel = new JPanel();
        updateGroups();
        add(groupPanel);
        messageList = new JList<>();
        updateMessageList();
        messageList.setName("messages");
        messageList.setSize(500, 1000);
        add(messageList);
        addUserInput();
        setName(user.getName());
        setSize(500, 500);
        setLayout(new GridLayout(5, 1));
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e)
            {
                chat.removeUser(user);
                chat.getGroups().forEach(group -> {
                    group.remove(user);
                    if (group.isEmpty()) {
                        chat.removeGroup(group);
                    }
                });
                super.windowClosing(e);
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

    private void updateGroups()
    {
        ButtonGroup groupButtons = new ButtonGroup();
        chat.getGroups().forEach(group -> {
            if (group.contains(user)) {
                JRadioButton groupButton = new JRadioButton(group.getName(), false);
                groupButtons.add(groupButton);
                groupPanel.add(groupButton);
            }
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
        chat.message(user.getName() + ": " + message, getSelectedUserNames());
    }

    /**
     * Return all selected users from the check boxes.
     */
    private List<User> getSelectedUsers()
    {
        List<User> users = new ArrayList<>();
        for (Component component : userPanel.getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox box = (JCheckBox)component;
                if (box.isSelected()) {
                    users.add(chat.getUsers().get(box.getText()));
                }
            }
        }
        return users;
    }

    /**
     * Return the names of all selected users from the check boxes.
     */
    private String[] getSelectedUserNames()
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
        public void update(Observable o, Object obj)
        {
            if (obj instanceof ChangeMode) {
                switch ((ChangeMode)obj) {
                case USER:
                    userPanel.setVisible(false);
                    userPanel.removeAll();
                    updateUsers();
                    userPanel.setVisible(true);
                    break;
                case MESSAGE:
                    updateMessageList();
                    break;
                case GROUP:
                    groupPanel.setVisible(false);
                    groupPanel.removeAll();
                    updateGroups();
                    groupPanel.setVisible(true);
                    break;
                }
            }
        }
    }
}
