# Advanced Software Enineering Project (DHBW Stuttgart)
## A chat simulation


# Content
### 1. [What it does](#1)
### 2. [Design](#design)
### 3. [Implementation](#Implementation)
### 4. [Testing](#testing)
### 5. [Sources](#5)

-----

# What it does <a name="1"><a/>

This project demonstrates a chat application on one local computer. Once the project is build and started, there is a so called "Main Window". It provides a method for new 'users' to join the chat. After inserting a name which is not yet taken by another user and confirmation, a new window will open, showing the chat client for the new user.
Each client window has a list with all active users in the top left. The user of that window<sup>1</sup> can select who sould receive the next message he/she sends.
On the top right side one can manage groups. A group contains all currently selected users. The user can see all the groups he is part of. So, by clicking on a group the just described list of usernames will be auto-selected according to the group. **For the same selection of users** two groups can't have the same name.

Below all of that is the main part: the actual chat. Here all the messages from all users will be displayed as long as the user was selected to see it (by the sender). That implies that if a new user joins, there won't be any new messages, since no one could send anything to him before.
Below that there is a simple input field to type in a new message. The message is sent after hitting the return key. 
And on the bottom left there is a button to change the theme for the client between dark-mode and light-mode. 

Lastly, once a user is done chatting and closes the client window, all other clients will be updated and remove that user from their list. If the main window is closed, all client windows will be closed as well and the application will be terminated.

<br/>
<sup>1</sup>
The username for each client window is displayed in the title bar of that window and right next to the input field on the bottom.

# Design
The user interface of this application was made in large portions with the Java Swing framework, which is part of the Java Foundation Classes. This framework makes it possible to design and develop responsive program that according to the *Java look and feel* "that looks the same on all platforms". [1]

To further improve the design of the application it was decided to use MiG Layout, an open source layout manager for Java and Swing. It helps to dynamically set the size and position of objects, based on certain rules. This is more reliable, easier to understand and most importantly, it helps avoid bugs in the UI. [2]

Furthermore, this project uses a few open source image icons to make this application more user-friendly and intuitive. In addition, an option was implemented for each chat participant to change the theme of the user interface between light-mode and dark-mode. All this has no direct effect on the core-functionality and was just implemented to help the user understand and use this application.

# Implementation
 The `Chat`, `Group` and `User` can be serialized to and deserialized from JSON. The file is stored at the project's root or from where the jar was executed.

### Design Patterns <a name="4"><a/>
 The `Chat` and `User` are observable objects. The client window object uses this observer pattern to react to changes in the objects. With this solution the UI and business logic can be separated easily without tightly coupling them. Furthermore multiple observers can be registered on an object. [3]

All POJOs are designed following the fluent interface design pattern. This means that all setters or other methods normally returning `void` return an instance of themselves. This has been done so method calls can be chained to make the code more readable. [4]

# Testing

The project is built with maven which makes it easy to manage dependencies and plugins. Maven was used to run the tests, check the test coverage and run code quality checks on the project. The build is executed on a virtual Ubuntu machine in CircleCI which is a build platform that can be integrated into a GitHub repository easily. The configuration for the automatic build is located in [`.circleci/config.yml`](.circleci/config.yml). For each push of a branch to the repository a build is triggered. A failing build will be documented in the pull request and the changes will not be merged into the master branch, which is always a stable version of the application without bugs.

### Unit Tests

All classes not related to UI have been covered by unit tests running with JUnit 5. Test classes are identified by maven with the file ending `Test`, which is a maven naming convention. The code to test ratio can be seen here: [![codecov](https://codecov.io/gh/ingokuba/swing-chat/branch/master/graph/badge.svg)](https://codecov.io/gh/ingokuba/swing-chat)

But test coverage alone is not sufficient. On top of that test methods have to be able to run indepentently and not change the state of the application. This is ensured by cleaning the test base after each test, e.g. when a file is stored in a test it has to be deleted afterwards, regardless if the test failed.

### UI Tests

Use cases of the UI are tested in integration tests, the Java classes end with `IT` (Integration Test). The running of the swing application is done with a framework called `assertj` which wraps UI components, so they can be interacted with. It also supports assertions by itself, which were seldom used when writing the test, because the focus was on JUnit assertions.

### Sonar

To check bugs, code smells and vulnerability the code is checked with Sonar to ensure code quality: 

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ingokuba_swing-chat&metric=alert_status)](https://sonarcloud.io/dashboard?id=ingokuba_swing-chat)


# Sources
[1] Oracle, 2019. The Java™ Tutorials - How to Set the Look and Feel. https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html

[2] MiG InfoCom AB, 2011. MigLayout - Java Layout Manager for Swing, SWT and JavaFX. http://www.miglayout.com

[3] w3sDesign, 2014. Observer design pattern. http://w3sdesign.com/?gr=b07&ugr=proble

[4] Martin Fowler, 2005. FluentInterface. https://www.martinfowler.com/bliki/FluentInterface.html