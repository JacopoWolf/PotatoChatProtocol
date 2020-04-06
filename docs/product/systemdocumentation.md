<center>

<h1>System Documentation</h1>

</center>

For **PotatoChatProtocol**, version 1.0 approved

Prepared by [@Alessio789](https://github.com/Alessio789)

Server Group:

- [@JacopoWolf](https://github.com/JacopoWolf)
- [@gfurri20](https://github.com/gfurri20)
- [@Alessio789](https://github.com/Alessio789)

--- 

## 1. Introduction
PotatoChatProtocol is a simple protocol that allows multiple users to communicate with each other in a local network, in public and private rooms.
It also offers APIs for an optimized and easily expandable server that allows the correct interaction to different users.

---

## 2. Install on Simulator or Device

### 2.1 Required Components 
To start-up a PCP server the device requires a Java version 8 (recommended) or higher and JDK version 8 (recommended) or higher. The protocol is very light, if you implement a small chat you can start the server on most multi cores processors. When the size increases accordingly the number of threads used will increase, this requires better performing processors. The minimum amount of RAM required is 4 GB.

### 2.2 Installation Code
Note: the word "TAG" must be replaced with the version (eg "Min.1.0")

#### 2.2.1 Maven
1. Add the repository:
    ```xml
    <repositories>

        <repository>
    		<id>jitpack.io</id>
    		<url>https://jitpack.io</url>

    	</repository>

    </repositories>
    ```

2. Add the dependency
    ```xml
    <dependencies>

    	<dependency>

    		<groupId>com.github.JacopoWolf</groupId>
    		<artifactId>PotatoChatProtocol</artifactId>
    		<version>TAG</version>

    	</dependency>

    </dependencies>
    ```

#### 2.2.2 Gradle
1. Add the JitPack repository in your root build.gradle at the end of repositories:
    ```groovy
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
    ```
2. Add the dependency:
    ```groovy
    dependencies {
	        implementation 'com.github.JacopoWolf:PotatoChatProtocol:Tag'
	}
    ```

#### 2.2.3 sbt
1. Add it in your build.sbt at the end of resolvers:
    ```scala
    resolvers += "jitpack" at "https://jitpack.io"
    ```
 
2. Add the dependency
    ```scala
    libraryDependencies += "com.github.JacopoWolf" % "PotatoChatProtocol" % "Tag"	
    ```

#### 2.2.4 Leiningen
1. Add it in your project.clj at the end of repositories:
    ```clojure
     :repositories [["jitpack" "https://jitpack.io"]]
    ```

2. Add the dependency
    ```clojure
    :dependencies [[com.github.JacopoWolf/PotatoChatProtocol "Tag"]]
    ```

#### 2.2.5 After adding the dependency
1. Create a main class in your project and type: 
    ```java 
    import PCP.services.PCPServer;
    ```
2. Instantiate the server and run it with 
    ```java
    PCPServer server = new PCPServer();
    server.acceptAndServe();
    ```
---
## 3. System Maintenance
The PCPServer has no graphical interface. Once the application has started, you can see the server logs. The server logs show whatever the server is doing: new connections, disconnections, messages sent...
