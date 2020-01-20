<center>
<h1>Usage guide</h1>
</center>


# Overview

## Description
The PCP-Minimal version of the PCP protocol is its stupid version, implementing an extremely easy, unprotected, channel-based and volatile LAN chat.

This is a short tutorial on how to use the APIs of this simple chat.

## [javadoc](https://javadoc.jitpack.io/com/github/jacopowolf/potatochatprotocol/latest/javadoc/index.html)
This project is published with [JitPack](https://jitpack.io/), JavaDocs and releases are hosted on their servers for everyone to use.


# API structure
The root package, `PCP` contains the common important variables:

- `PCP`: static class containing common protocol variables, like **PCP.PORT** or the **Versions** enumerator
- `OpCode`: enumerator conaining all operational codes, convertable to their respective *byte*, and a function to retrive the opcode from a byte.
- `PCPException`: manages all possible errors planned by the protocol.

The API is then divided in 4 main generic packages:

- `services`: high level APIs who expose simple-to-use services, like **PCPServer**
- `logic`: logic and data layers, includes interfaces to implement correctly the logical level of the server and to access data.
- `data`: common data types. **PCPVariablePayload** is a really useful class to extend.
- `net`: net, connection, and worklaod layers. contains **PCPChannel** and **PCPManager**

Version-specific packages, named with their version name, like `Min` contain their specific implementation of said root packages.
Again, they are organized in the same way. For example the PCP-Min implementation of **IPCPUserInfo** is located under `PCP.Min.logic`.

# Services

## PCPServer
`PCP.services.PCPServer` is the main implementation of a PCP server.

It uses the default gobal logger to log messages, wich can be obtained with `Logger.GetGlobal()`.

If you have performance problems, it might be your system is not good at managing threads, as this implementation allocates at least 7 of them.

### start

To start a server write down in a meaven project these three lines of code.

In the below example, a PCPServer is started on the loopback interface.

```java
InetAddress address = InetAddress.getByName("127.0.0.1");
PCPServer server = new PCPServer( address );
server.acceptAndServe();
```
be careful as all of those calls can throw an `IOException`.
If the reson of this exception is a port already in use, make sure you don't have any other application unfortunately already occupying the port **53101**. If that's the case, restarting your computer might solve the issue.

### logging
PCPServer uses Java's `Logger.getGlobal()` to log every operation it does.
By default, the global logger prints on the console with an `INFO` logging level. 

If you want to change logging output, go look at Oracle's official [documentation](https://docs.oracle.com/javase/8/docs/api/java/util/logging/Logger.html) for complete usage information.

To change the logging level to `FINEST`, allowing detailed printing of every action performed by the server, write those two instructions:
```java
// changes default console logging level
Logger.getLogger("").getHandlers()[0].setLevel(Level.FINEST);
// changes global configuration logging level
Logger.getGlobal().setLevel(Level.FINEST);
```

### interrupt

to interrupt the server the proceedure is simple:

```java
server.shutDown();
```

this is a blocking call that will safely shutdown the server, meaning it might take some time.
