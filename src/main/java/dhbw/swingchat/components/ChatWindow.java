package dhbw.swingchat.components;

import static dhbw.swingchat.helper.MessageUtil.showWarning;
import static dhbw.swingchat.storage.Storage.getPngImage;
import static javax.swing.JOptionPane.showInputDialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import dhbw.swingchat.helper.ThemedJFrame;
import dhbw.swingchat.instance.Chat;
import dhbw.swingchat.instance.Group;
import dhbw.swingchat.instance.User;
import net.miginfocom.swing.MigLayout;

/**
 * Chat window for a specific user.
 */
public class ChatWindow extends ThemedJFrame
{

    private static final long        serialVersionUID = 1L;

    private transient User           user;
    private transient Chat           chat;

    private JPanel                   userPanel;
    private JPanel                   groupPanel;
    private JList<String>            messageList;
    private JPanel                   inputPanel;

    private ButtonGroup              groupButtons     = new ButtonGroup();
    private DefaultListModel<String> messages         = new DefaultListModel<>();

    /**
     * Build a new chat window for a given user.
     * 
     * @param user1 User of this window.
     * @param chat1 Reference to the chat object of the application.
     */
    public ChatWindow(User user1, Chat chat1)
    {
        setLayout(new MigLayout("fill"));
        this.user = user1;
        this.user.addPropertyChangeListener(new UserListener());
        this.chat = chat1;
        this.chat.addPropertyChangeListener(new ChatListener());
        userPanel = new JPanel(new MigLayout());
        addUsers();
        add(userPanel);

        // new group button:
        addCreateGroupButton();

        groupPanel = new JPanel(new MigLayout());
        addGroups();
        add(groupPanel, "wrap");

        messageList = new JList<>();
        for (String message : user.getMessages()) {
            messages.addElement(message);
        }
        messageList.setModel(messages);
        messageList.setName("messages");
        add(messageList, "grow, push, span");

        inputPanel = new JPanel(new MigLayout("fill"));
        addUserLabel();
        addUserInput();
        add(inputPanel, "growx, pushx, span");

        addChangeThemeButton();

        setName(user.getName());
        setTitle(user.getName());
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e)
            {
                updateFrame();
            }

            @Override
            public void windowClosing(WindowEvent e)
            {
                chat.removeUser(user);
                List<Group> copy = new ArrayList<>();
                copy.addAll(chat.getGroups());
                copy.forEach(group -> {
                    if (group.contains(user)) {
                        group.remove(user);
                        if (group.size() == 0) {
                            chat.removeGroup(group);
                        }
                    }
                });
                super.windowClosing(e);
            }
        });
    }

    /**
     * Add all user checkboxes to the frame.
     */
    private void addUsers()
    {
        chat.getUsers().forEach(this::addUser);
    }

    /**
     * Add a user checkbox with the name of the given user.
     */
    private void addUser(User user)
    {
        String name = user.getName();
        JCheckBox box = new JCheckBox();
        box.setSelected(true);
        box.setAction(new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                groupButtons.clearSelection();
            }
        });
        box.setName(name);
        userPanel.add(box, "wrap");
        box.setText(name);
    }

    /**
     * Add button to change the UI theme to the frame.
     */
    private void addChangeThemeButton()
    {
        JButton themeBtn = new JButton();
        themeBtn.setName("changeTheme");
        themeBtn.setOpaque(true);
        themeBtn.setBorderPainted(false);
        themeBtn.setAction(new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                toggleTheme();
                updateThemeButton(themeBtn);
            }
        });

        updateThemeButton(themeBtn);
        add(themeBtn);
    }

    /**
     * Update the button depending on selected color theme.
     * 
     * @param themeBtn Button to change color theme.
     */
    private void updateThemeButton(JButton themeBtn)
    {
        Color chatColor = getSecondaryColor();
        this.messageList.setBackground(chatColor);

        String imageName = this.getDarkModeBoolean() ? "light" : "dark";
        Image themeIcon = getPngImage(imageName);

        String name = this.getDarkModeBoolean() ? "Light Mode" : "Dark Mode";

        if (themeIcon != null) {
            themeBtn.setIcon(new ImageIcon(themeIcon.getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
            themeBtn.setToolTipText(name);
        }
        else {
            themeBtn.setText(name);
        }
    }

    /**
     * Add the button for creating groups to the frame.
     */
    private void addCreateGroupButton()
    {
        JButton addGroup = new JButton();
        addGroup.setOpaque(true);
        addGroup.setBorderPainted(false);
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

                if (groupName == null) { //Cancel / Escape / Close
                    return;
                }
                if (groupName.isEmpty()) {
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
        Image groupIcon = getPngImage("group_add");
        if (groupIcon != null) {
            addGroup.setIcon(new ImageIcon(groupIcon.getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
            addGroup.setToolTipText("New group");
        }
        else {
            addGroup.setText("New group");
        }
    }

    /**
     * Add all group buttons to the frame.
     */
    private void addGroups()
    {
        chat.getGroups().forEach(this::addGroup);
    }

    /**
     * Add a group button for the given group.
     */
    private void addGroup(Group group)
    {
        if (group.contains(user)) {
            String groupName = group.getName();
            JToggleButton groupButton = new JToggleButton();
            groupButton.setOpaque(true);
            groupButton.setBorderPainted(false);
            groupButton.setName(groupName);
            groupButtons.add(groupButton);
            groupButton.setAction(new AbstractAction() {

                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    for (Component component : userPanel.getComponents()) {
                        JCheckBox box = (JCheckBox)component;
                        box.setSelected(group.contains(box.getName()));
                    }
                }
            });
            groupPanel.add(groupButton, "wrap, growx");
            groupButton.setText(groupName);
            groupButton.setToolTipText(group.toString());
        }
    }

    /**
     * Add the username in front of the input field.
     */
    private void addUserLabel()
    {
        JLabel userName = new JLabel();
        inputPanel.add(userName);
        userName.setText(user.getName() + ": ");
    }

    /**
     * Add the user input field.
     */
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
            JCheckBox box = (JCheckBox)component;
            if (box.isSelected()) {
                users.add(chat.getUser(box.getName()));
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
            JCheckBox box = (JCheckBox)component;
            if (box.isSelected()) {
                names.add(box.getName());
            }
        }
        return names.toArray(new String[0]);
    }

    /**
     * Reacts to changes in the user object.
     */
    private class UserListener
        implements PropertyChangeListener
    {

        @Override
        public void propertyChange(PropertyChangeEvent event)
        {
            String message = String.valueOf(event.getNewValue());
            messages.addElement(message);
        }
    }

    /**
     * Reacts to changes in the chat object.
     */
    private class ChatListener
        implements PropertyChangeListener
    {

        @Override
        public void propertyChange(PropertyChangeEvent event)
        {
            String property = event.getPropertyName();
            if ("users".equalsIgnoreCase(property)) {
                userPanel.setVisible(false);
                User oldUser = (User)event.getOldValue();
                if (oldUser == null) {
                    addUser((User)event.getNewValue());
                    updateFrame();
                }
                else {
                    removeUser(oldUser);
                }
                userPanel.setVisible(true);
            }
            if ("groups".equalsIgnoreCase(property)) {
                groupPanel.setVisible(false);
                Group group = (Group)event.getOldValue();
                if (group == null) {
                    addGroup((Group)event.getNewValue());
                    updateFrame();
                }
                else {
                    removeGroup(group);
                }
                groupPanel.setVisible(true);
            }
        }

        /**
         * Remove user checkbox from the user panel.
         * 
         * @param user User to look for.
         */
        private void removeUser(User user)
        {
            for (Component component : userPanel.getComponents()) {
                if (component.getName().equals(user.getName())) {
                    userPanel.remove(component);
                }
            }
        }

        /**
         * Remove group button from the group panel.
         * 
         * @param group Group to look for.
         */
        private void removeGroup(Group group)
        {
            for (Component component : groupPanel.getComponents()) {
                if (component.getName().equals(group.getName())) {
                    groupPanel.remove(component);
                }
            }
        }
    }

    @Override
    public void updateFrame()
    {
        super.updateFrame();
        messageList.setBackground(getSecondaryColor());
    }
}
