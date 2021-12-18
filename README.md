# CS 3733-D21 Fuchsia Falcons (Team F) - Project

This application represents the collective effort of an 11 person team over a 7 week term in Software Engineering at WPI in Spring of 2021. Our group was tasked with creating a desktop/kiosk application for use at Brigham and Women's Hospital, and were given multiple requirements including a pathfinding system, a map editor, service requests, and a system allowing for the creation, deletion, and maintanence of users. The UI was made in JavaFX using JFoenix, and the backend database was constructed with Apache Derby.

----------

## Remote Apache Derby Server Information

When running our application please be on the WPI wifi, or have an IP address within the WPI address space.

For security reasons, only devices in the WPI IP space can access our VPS.

## Environment & Keys

For security purposes, this project is missing the .env file with the required keys for the project (APIs & etc). We included this file in our submission, however, if there is an issue, please contact us. 

## Contact For Issues

Please contact our lead software engineer, [ahfriedman](https://github.com/ahfriedman), with any errors regarding the server.

NOTE: The server is intentionally down at the moment, and when the application is run in offline mode it will create a local Apache Derby database on your machine.


## Important Files
### build.gradle / gradle.properties
This is the gradle configuration file. Modify this file to add dependencies to your project. In
 general you should only modify the depedencies section of this file, however there are a few
  modification you will need to make when you begin the project
  
  - `mainClassName`
    - Modifiy this variable to point to your main class. By default it is `Main
    `, but once you update your teamname package to your team letter you will need to update this
     path.



### .gitignore
This file tells git which files to ignore in the repo. It should be correctly configured already
**Only modify this file if you are sure you know what you are doing**


### resources/edu/wpi/fuchsiafalcons/fxml
Here you will find the Fxml files for the default scenes. This method of accessing resources is how we recommend you organize all your FXML for the project.

## Building a jar
To assemble a jar file for your project, run the "jar" gradle task, either through IntelliJ or by doing
`gradle jar` on a terminal. Gradle will automatically download all dependencies needed to compile your jar file,
which will be stored in the build/libs folder.

Make sure to edit the main class attribute the build.gradle file, you'll need to change it in order to obtain
a working jar file.





