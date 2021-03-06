# Swing Chat [![CircleCI](https://circleci.com/gh/ingokuba/swing-chat.svg?style=svg)](https://circleci.com/gh/ingokuba/swing-chat) [![codecov](https://codecov.io/gh/ingokuba/swing-chat/branch/master/graph/badge.svg)](https://codecov.io/gh/ingokuba/swing-chat) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ingokuba_swing-chat&metric=alert_status)](https://sonarcloud.io/dashboard?id=ingokuba_swing-chat)

:computer: Simulation of a chat program for multiple users. A documentation of the project can be found [here](DOCUMENTATION.md).

## Running the project

### Development

Execute [`dhbw.swingchat.Main`](src/main/java/dhbw/swingchat/Main.java).

### Maven

Building the jar:

```
mvn package
```

the generated jar is located in `target/swing-chat.jar`

execute with:

```
java -jar swing-chat.jar
```

## Storage

The state of the application is persisted in a json file (`Chat.json`) located relative from the location of the project or from where the jar is executed.