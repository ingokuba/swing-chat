package dhbw.swingchat.components;

import static dhbw.swingchat.components.MessageUtil.showWarning;
import static javax.swing.JOptionPane.showInputDialog;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dhbw.swingchat.ChangeMode;
import dhbw.swingchat.instance.Chat;
import dhbw.swingchat.instance.Group;
import dhbw.swingchat.instance.User;
import net.miginfocom.swing.MigLayout;

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
    private JPanel            inputPanel;

    public ChatWindow(User user1, Chat chat1)
    {
        setLayout(new MigLayout("fill"));
        this.user = user1;
        this.user.addObserver(new ChatObserver());
        this.chat = chat1;
        this.chat.addObserver(new ChatObserver());
        userPanel = new JPanel(new MigLayout());
        updateUsers();
        add(userPanel);
        // new group button:
        addGroupButton();

        groupPanel = new JPanel(new MigLayout());
        updateGroups();
        add(groupPanel, "wrap");

        messageList = new JList<>();
        updateMessageList();
        messageList.setName("messages");
        add(messageList, "grow, push, span");

        inputPanel = new JPanel(new MigLayout("fill"));
        addUserLabel();
        addUserInput();
        add(inputPanel, "growx, pushx, span");

        setName(user.getName());
        setTitle(user.getName());
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e)
            {
                chat.removeUser(user);
                List<Group> copy = new ArrayList<>();
                copy.addAll(chat.getGroups());
                copy.forEach(group -> {
                    group.remove(user);
                    if (group.size() == 0) {
                        chat.removeGroup(group);
                    }
                });
                super.windowClosing(e);
            }
        });
    }

    private void updateUsers()
    {
        chat.getUsers().forEach(chatMember -> {
            String name = chatMember.getName();
            JCheckBox box = new JCheckBox(name, true);
            box.setName(name);
            userPanel.add(box, "wrap");
        });
    }

    private void addGroupButton()
    {
        JButton addGroup = new JButton();
        addGroup.setName("newGroup");
        addGroup.setAction(new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                List<User> selectedUsers = getSelectedUsers();
                if (selectedUsers.isEmpty()) {
                    showWarning(addGroup, "Group cannot be empty");
                    return;
                }
                String groupName = showInputDialog("Enter group name");
                if (groupName == null || groupName.isEmpty()) {
                    showWarning(addGroup, "Group name cannot be empty");
                    return;
                }
                Group group = new Group(groupName, selectedUsers);
                if (chat.getGroup(group) == null) {
                    chat.addGroup(group);
                }
                else {
                    showWarning(addGroup, "Same group already exists");
                }
            }
        });
        add(addGroup);
        Image groupIcon = getGroupIcon();
        if (groupIcon != null) {
            addGroup.setIcon(new ImageIcon(groupIcon.getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
            addGroup.setToolTipText("New group");
        }
        else {
            addGroup.setText("New group");
        }
    }

    private Image getGroupIcon()
    {
        try {
            return ImageIO.read(getClass().getResource("/img/group_add.png"));
        } catch (Exception ex) {
            return null;
        }
    }

    private void updateGroups()
    {
        ButtonGroup groupButtons = new ButtonGroup();
        chat.getGroups().forEach(group -> {
            if (group.contains(user)) {
                String groupName = group.getName();
                JButton groupButton = new JButton();
                groupButton.setName(groupName);
                groupButtons.add(groupButton);
                groupButton.setAction(new AbstractAction() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        for (Component component : userPanel.getComponents()) {
                            if (component instanceof JCheckBox) {
                                JCheckBox box = (JCheckBox)component;
                                box.setSelected(group.contains(box.getText()));
                            }
                        }
                    }
                });
                groupPanel.add(groupButton, "wrap, growx");
                groupButton.setText(groupName);
                groupButton.setToolTipText(group.toString());
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

    private void addUserLabel()
    {
        JLabel userName = new JLabel();
        inputPanel.add(userName);
        userName.setText(user.getName() + ": ");
    }

    private void addUserInput()
    {
        JTextField userInput = new JTextField();
        userInput.setLayout(new MigLayout());
        userInput.setName("userInput");
        userInput.setSize(200, 50);
        userInput.setAction(new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                String message = userInput.getText();
                if (!message.isEmpty()) {
                    message(message);
                    userInput.setText("");
                }
            }
        });
        inputPanel.add(userInput, "growx, pushx, span");
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
                    users.add(chat.getUser(box.getText()));
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
