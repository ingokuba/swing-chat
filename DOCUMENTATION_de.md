# [:gb:](DOCUMENTATION.md)

# Advanced Software Engineering Projekt (DHBW Stuttgart)
## Eine Chat Simulation


# Inhalt
### 1. [Funktion](#Funktion)
### 2. [Design](#Design)
### 3. [Implementierung](#Implementierung)
### 4. [Tests](#Tests)
### 5. [Quellen](#Quellen)

-----

# Funktion

Dieses Projekt demonstriert eine Chat-Anwendung auf einem lokalen Computer. Sobald das Projekt gebaut und gestartet wurde, erscheint ein sogenanntes "Hauptfenster". Es bietet eine Funktion, womit neue Benutzer dem Chat beitreten können. Nach Eingabe eines Namens, der noch nicht von einem anderen Benutzer übernommen wurde, und Bestätigung öffnet sich ein neues Fenster, das den Chat-Client für den neuen Benutzer anzeigt.

Jedes Clientfenster hat oben links eine Liste mit allen aktiven Benutzern. Der Benutzer dieses Fensters <sup>1</sup> kann auswählen, wer die nächste Nachricht, die er/sie sendet, erhalten soll.
Oben rechts kann man Gruppen verwalten. Eine neue Gruppe enthält alle aktuell ausgewählten Benutzer. Der Benutzer kann alle Gruppen sehen, in denen er Mitglied ist. Wenn man also auf eine Gruppe klickt, wird die soeben beschriebene Liste von Benutzernamen automatisch entsprechend der Gruppe ausgewählt. **Bei der gleichen Auswahl von Benutzern** können zwei Gruppen nicht den gleichen Namen haben.
Unter all dem ist der Hauptteil: der eigentliche Chat. Hier werden alle Nachrichten von allen Benutzern angezeigt, solange der Benutzer (vom Absender) ausgewählt wurde, um sie zu sehen. Das bedeutet, wenn ein neuer Benutzer beitritt, es für diesen keine Nachrichten geben wird, da ihm vorher niemand etwas schicken konnte.
Darunter gibt es ein einfaches Eingabefeld, um eine neue Nachricht einzugeben. Die Nachricht wird nach Drücken der Eingabetaste gesendet. 
Unten links gibt es einen Button, um das Aussehen für den Client zwischen Hell- und Dunkelmodus zu wechseln. 

Sobald ein Benutzer fertig mit dem Chatten ist und das Clientfenster schließt, werden alle anderen Clients aktualisiert und der Benutzer aus der Liste entfernt. Wenn das Hauptfenster geschlossen wird, werden alle Clientfenster ebenfalls geschlossen und die Anwendung wird beendet.

<br/>
<sup>1</sup> Der Benutzername für jedes Clientfenster wird in der Titelleiste des jeweiligen Fensters und rechts neben dem Eingabefeld am unteren Rand angezeigt.

# Design
Die Benutzeroberfläche dieser Anwendung wurde in großen Teilen mit dem Java Swing Framework erstellt, das Teil der Java Foundation ist. Dieses Framework ermöglicht es, responsive Programme zu entwerfen und zu entwickeln, die nach dem *Java Look and Feel* "auf allen Plattformen gleich aussehen". [1]

Um das Design der Anwendung weiter zu verbessern, wurde MiG Layout benutzt, ein Open-Source-Layoutmanager für Java Swing. Er hilft, die Größe und Position von Objekten nach bestimmten Regeln dynamisch festzulegen. Dies ist zuverlässiger, leichter zu verstehen und vor allem hilft es, Fehler in der Benutzeroberfläche zu vermeiden. [2]

Außerdem verwendet dieses Projekt einige Open-Source-Symbole, um die Anwendung benutzerfreundlicher und intuitiver zu gestalten. Zusätzlich wurde für jeden Chatteilnehmer eine Möglichkeit implementiert, das Aussehen der Benutzeroberfläche zwischen Hell- und Dunkelmodus zu wechseln. All dies hat keinen direkten Einfluss auf die Kernfunktionalität und wurde nur implementiert, um dem Benutzer das Verständnis und die Nutzung dieser Anwendung zu erleichtern.

# Implementierung
 Die Java-Objekte `Chat`, `Group` und `User` können zu JSON serialisiert und von JSON deserialisiert werden. Diese Datei wird im Projekt oder dort, von wo das Jar ausgeführt wurde, gespeichert.

### Design Patterns
 Der `Chat` und der `User` sind beobachtbare Objekte (engl. observable), was durch das Interface `PropertyChangeListener` umgesetzt wurde. Das Clientfenster verwendet dieses "Observable" Design-Pattern, um auf Veränderungen der Objekte zu reagieren. Mit dieser Lösung lassen sich UI und Geschäftslogik einfach trennen, ohne sie eng miteinander zu verknüpfen. Außerdem können mehrere Listener auf einem Objekt registriert werden. [3]

Alle Java-Objekte wurden nach dem "Fluent Interface" Designpattern entworfen. Das bedeutet, dass alle Setter oder andere Methoden, die normalerweise `void` zurückgeben, eine Instanz von sich selbst zurückgeben. Dies wurde getan, damit Methodenaufrufe verkettet werden können, um den Code lesbarer zu machen. [4]

# Tests

Das Projekt wird mit Maven gebaut, was es einfach macht Abhängigkeiten und Plugins zu verwalten. Maven wurde verwendet um die Tests durchzuführen, die Testabdeckung zu überprüfen und die Qualitätsprüfungen für den Code des Projekts durchzuführen. Der Build wird auf einer virtuellen Ubuntu Maschine in CircleCI ausgeführt, welches eine Build-Plattform ist, die einfach in ein GitHub Repository integriert werden kann. Die Konfiguration für den automatischen Build befindet sich in [`.circleci/config.yml`](.circleci/config.yml). Für jeden Push eines Branches in das Repository wird ein Build ausgelöst. Ein fehlgeschlagener Build wird im Pull-Request dokumentiert und die Änderungen werden nicht in den Masterbranch übernommen, der immer eine stabile Version der Anwendung ohne Bugs ist.

### Unit-Tests

Alle Klassen, die nicht mit der Oberfläche zusammenhängen, wurden durch Unit-Tests mit JUnit 5 getestet. Die Testklassen werden durch Maven mit der Dateiendung `Test` identifiziert, was eine Maven-Namenskonvention ist. Das Verhältnis von Code zu Test kann hier eingesehen werden: [![codecov](https://codecov.io/gh/ingokuba/swing-chat/branch/master/graph/badge.svg)](https://codecov.io/gh/ingokuba/swing-chat)

Jedoch reicht Testabdeckung allein nicht aus. Darüber hinaus müssen Testmethoden in der Lage sein, unabhängig voneinander zu laufen und den Zustand der Anwendung nicht zu verändern. Dies wird durch eine Bereinigung der Testbasis nach jedem Test sichergestellt, z.B. wenn eine Datei in einem Test gespeichert wird, muss sie anschließend gelöscht werden, unabhängig davon, ob der Test fehlgeschlagen ist.

### Oberflächentests

Die Use Cases der Oberfläche werden in Integrationstests getestet, die Javaklassen enden hierbei mit `IT` (Integrations-Test). Die Ausführung der Swing-Anwendung erfolgt mit einem Framework namens `assertj`, das Komponenten der Oberfläch wrapped, so dass mit ihnen interagiert werden kann. Es unterstützt auch Assertions an sich, die beim Schreiben der Tests nur selten verwendet wurden, da der Schwerpunkt auf JUnit-Assertions lag.

### Sonar

Um Fehler, Unreinheiten und Schwachstellen im Code zu überprüfen, wird dieser mit Sonar überprüft, um die Qualität sicherzustellen:

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ingokuba_swing-chat&metric=alert_status)](https://sonarcloud.io/dashboard?id=ingokuba_swing-chat)


# Quellen
[1] Oracle, 2019. The Java™ Tutorials - How to Set the Look and Feel. https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html

[2] MiG InfoCom AB, 2011. MigLayout - Java Layout Manager for Swing, SWT and JavaFX. http://www.miglayout.com

[3] w3sDesign, 2014. Observer design pattern. http://w3sdesign.com/?gr=b07&ugr=proble

[4] Martin Fowler, 2005. FluentInterface. https://www.martinfowler.com/bliki/FluentInterface.html
