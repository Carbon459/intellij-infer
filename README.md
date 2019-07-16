# intellij-infer
An Integration of the [Infer static analyzer](https://fbinfer.com/) for the IntelliJ Platform

# How to Use
Prerequisites:
 - IntelliJ IDEA 2019.1 (Edition doesn't matter)
 - Gradle Plugin
 - gradle-intellij Plugin
 - CLion 2019.1
 
 1. Clone the Repository
 2. In the build.gradle file with the Line:
    `compileOnly files(PATH)`
    Replace PATH with the Path to the clion.jar of your CLion Installation (usually in the lib directory of the clion main directory)
 3. Run the 'runIde' task in Gradle
