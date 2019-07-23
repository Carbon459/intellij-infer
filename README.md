# intellij-infer
An Integration of the [Infer static analyzer](https://fbinfer.com/) for IntelliJ IDEA and CLion

![intellij-infer screenshot](https://raw.githubusercontent.com/Carbon459/intellij-infer/master/src/main/resources/META-INF/intellij-infer_screenshot.png)
## Features
- Launch an Infer Analysis via your IDE
- Select the desired Checkers
- Supports JavaC, Gradle, Maven, CMake(gcc or clang)
- Reactive Mode supported (only analyze changed files)
- Navigate through the Bugs or Bugtraces
- Manage Infer Installations

## How to Use
Prerequisites:
 - IntelliJ IDEA 2019.1 (Edition doesn't matter)
 - Gradle Plugin
 - gradle-intellij Plugin
 - CLion 2019.1
 
 1. Clone the Repository
 2. In the build.gradle file search for the following line:
    `compileOnly files(PATH)`
    
    Replace PATH with the Path to the clion.jar of your CLion Installation (usually in the lib directory of the clion main directory).
 3. Import the project into IntelliJ IDEA and let Gradle download all dependencies.
 4. Run the 'runIde' task in Gradle

Infer is available on Mac and Linux only
