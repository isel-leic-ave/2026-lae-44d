# **Lab Guide: JVM Ecosystem and Interoperability**

**Objectives:**
* Experiment with interoperability between Kotlin and Java on the JVM.
* Understand `.class` file generation, dependency resolution, `CLASSPATH`, and lazy class loading.
* Explore how Gradle manages dependencies and builds a runnable JVM application.

**This guide must be completed without IntelliJ. Use a lightweight text editor
such as Notepad, or Visual Studio Code, or other.**

---

## **Part 1:  Simple Java & Kotlin Interoperability**

### **Step 0: Setup**

1. Make sure the following command-line tools are installed and accessible from your terminal:

    * Java JDK (`javac`, `java`)
    * Kotlin compiler (`kotlinc`, `kotlin`)

2. Copy and paste the `.gitignore` file from your classroom GitHub repository
   into the root directory of your repository.

3. Create a working directory:

   ```bash
   mkdir lesson02-lab-part1
   cd lesson02-lab-part1
   ```

---

### **Step 1: Java classes**

Create two separate files named `Foo.java` and `App.java`, each with the following content:

<table>
<tr>
<td>

```java
// Foo.java
class X { 
  public void print() {
    System.out.println("I am X"); 
  }
}
interface Y { 
  void print();
}
class Z { 
  public void print() {
    System.out.println("I am Z"); 
  }
}
```

</td>
<td>

```java
// App.java
public class App {
    public static void main(String[] args) {
        System.out.println("Press ENTER to proceed.");
        System.console().readLine();
        new X().print();
    }
    public static void bar() {
        new Z().print();
    }
}
```


</td>
</tr>
</table>

Follow the instructions below using the command-line terminal and answer the corresponding questions:

1. Compile `Foo.java` using `javac Foo.java`.
    * How many `.class` files are generated? Why?
2. Compile `App.java` using `javac App.java`.
    * How many `.class` files are generated? Why?
3. Remove the file `Y.class`, then compile `App.java` again using `javac App.java`.
    * Do you observe any errors?
    * If so, why does the error occur? If not, explain why.
4. Remove the file `Z.class`, then compile `App.java` again using `javac App.java`.
    * Do you observe any errors?
    * If so, why does the error occur? If not, explain why.
5. Compile both `Foo.java` and `App.java` again.  
   Then remove the files `Y.class` and `Z.class`.  
   Run the application using `java App`.
    * Do you observe any errors? Why?
6. Remove the file `X.class`. Then run the application using `java App`.
    * At what moment do you observe an error (if any)?
    * Why does this error occur?
7. Compile `Foo.java` again and run `App` without recompiling it.  
   Then modify the `print` method of class `X` so that it prints `"I am X
   version 2"`.  
   Recompile only `Foo.java`, and run the application again using `java App`
   without recompiling `App.java`.
    * Explain the observed output.

---

### **Step 2: Kotlin main file**

Create `App.kt`:

```kotlin
fun main() {
    println("Press ENTER to proceed.")
    readLine()
    X().print()    // Using Java class
}
```

1. Compile Kotlin using `kotlinc App.kt`.
    * Do you observe any errors?
    * If so, why does the error occur? If not, explain why.
2. Compile Kotlin again, specifying classpath with `kotlinc -cp . App.kt`.
    * What `.class` file does Kotlin generate for the top-level function?
3. Run the application using `kotlin AppKt`.  
   Run the application again but now using `java AppKt`.
    * Explain the reason of the observed error?
4. Look for the location folder of the Kotlin SDK installation and include the
   path to the `kotlin-stdlib.jar` in classpath (through `-cp` option) to
   execute `AppKt` in command line with the `java` tool successfully.  
   Note you should include both in the classpath, the current folder and the `kotlin-stdlib.jar`,
   such as `-cp .:kotlin-stdlib.jar` or `-cp .;kotlin-stdlib.jar`, separating with `:` or `;` for windows based OS (replace `kotlin-stdlib.jar` with the full path).
    * Explain what the kotlin tool does automatically when running the program, and why this step is necessary when using java directly.

---

## **Part 2: Gradle Build Tool**

### **Step 0: Setup**

1. Make sure the following command-line tools are installed and accessible from your terminal:

    * Gradle

2. Create a working directory:

   ```bash
   mkdir lesson02-lab-part2
   cd lesson02-lab-part2
   ```

### **Step 1: Multi-module Project**

Create a new project by running the command `gradle init` and press Enter to
select the default options for all prompts, except for the implementation
language, which must be set to **Kotlin** instead of Java. The selected options
should be:

* Type of build to generate: Application
* Implementation language: Kotlin
* Target Java version: 21
* Project name: lesson02-lab-part2
* Application structure: Single application project
* Build script DSL: Kotlin
* Test framework: kotlin.test
* Generate build using new APIs and behavior: No

Follow the instructions below using the command-line terminal and answer the corresponding questions:

1. Edit the file located at `gradle/wrapper/gradle-wrapper.properties` and
   change the value of `distributionUrl` to:
   `https\://services.gradle.org/distributions/gradle-9.3.1-bin.zip`
2. Run `./gradlew build` (Linux/macOS) or `gradlew build` (Windows).
3. Change to the directory `app/build/distributions` and decompress the `app.zip` archive.
4. Navigate to the `bin` directory inside the decompressed archive and run `./app` (Linux/macOS) or `app.bat` (Windows). The application should run successfully, and you should observe the output `Hello World`.
5. How many JAR files are in the decompressed `lib` folder?
6. Which of those JAR files contains the application entry point? What is its complete content?
7. Edit the file `app/build.gradle.kts` and remove the line containing `implementation(libs.guava)`.
8. Run `./gradlew clean build` (Linux/macOS) or `gradlew clean build` (Windows).
9. Repeat instructions 3 to 6 and record your observations. Justify the differences.

### **Step 2: Add a Java module**

1. Create a new directory `demo` at the same level as the `app` directory:
   ```bash
   mkdir demo
   mkdir -p demo/src/main/java/org/example
   ```

2. Copy the `build.gradle.kts` file from the `app` directory to the `demo` directory:
   ```bash
   cp app/build.gradle.kts demo/build.gradle.kts
   ```

3. Edit the file `demo/build.gradle.kts` and remove the application-related configuration:
    * Remove the line `alias(libs.plugins.kotlin.jvm)` from the `plugins` block
    * Remove the entire `application` block (including the `mainClass` property)
    * Change the `plugins` block to only include:
      ```kotlin
      plugins {
          java
      }
      ```
    * Update or simplify the `dependencies` block to use only Java/JUnit dependencies (remove Kotlin-specific dependencies)

4. Edit the root `settings.gradle.kts` file and add the `demo` module:
    * Add a new line: `include("demo")`
    * The file should now include both modules:
      ```kotlin
      rootProject.name = "lesson02-lab-part2"
      include("app")
      include("demo")
      ```

5. Create a simple Java class in the `demo` module. Create a file `demo/src/main/java/org/example/Greeter.java` with the following content:
   ```java
   package org.example;
   
   public class Greeter {
       private final String name;
       
       public Greeter(String name) {
           this.name = name;
       }
       
       public String greet() {
           return "Hello from demo module, " + name + "!";
       }
   }
   ```

6. Add a dependency from the `app` module to the `demo` module. Edit `app/build.gradle.kts` and add the following line to the `dependencies` block:
   ```kotlin
   implementation(project(":demo"))
   ```

7. Create or modify the main Kotlin application file `app/src/main/kotlin/org/example/App.kt` to use the Java class from the `demo` module:
   ```kotlin
   package org.example
   
   fun main() {
       val greeter = Greeter("World")
       println(greeter.greet())
   }
   ```

8. Additionally, modify the test Kotlin application file `app/src/test/kotlin/org/example/AppTest.kt` to use the Java class from the `demo` module:
   ```kotlin
   package org.example

   import kotlin.test.Test
   import kotlin.test.assertNotNull

   class AppTest { 
        @Test fun appHasAGreeting() {
            val classUnderTest = Greeter("World")
            assertNotNull(classUnderTest.greet(), "app should have a greeting")
        }
   }
   ```

9. Run `./gradlew clean build` (Linux/macOS) or `gradlew clean build` (Windows) to build both modules.

10. Change to the directory `app/build/distributions` and decompress the `app.zip` archive (you may need to delete the previously decompressed `app` folder first).

11. Navigate to the `lib` directory inside the decompressed archive and list all JAR files.
    * How many JAR files are now in the `lib` folder?
    * Identify which JAR file belongs to the `app` module and which belongs to the `demo` module.
    * What are the names of these JAR files?

12. Execute the application from the `bin` directory by running `./app` (Linux/macOS) or `app` (Windows).
    * What output do you observe?
    * Explain how Gradle packaged both modules into the distribution.

13. Compare the contents of `app.jar` and `demo.jar`:
    * Use `jar tf app.jar` and `jar tf demo.jar` to list their contents.
    * Which `.class` files are in each JAR?
    * Explain the relationship between the two modules based on their contents.
