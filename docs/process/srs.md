<center>
<h1> Software Requirements Specification </h1>
</center>


For **PotatoChatProtocol**

Version 1.0 approved

Prepared by [@gfurri20](https://github.com/gfurri20)

Server Group:

- [@JacopoWolf](https://github.com/JacopoWolf) - Team leader
- [@gfurri20](https://github.com/gfurri20)
- [@Alessio789](https://github.com/Alessio789)

November 25, 2019

## 1 Introduction

### 1.1 Purpose
The purpose of this document is to present a detailed description of the PotatoChatProtocol standard. It will explain the purpose and features of the software, the interfaces of the software, what the software will do and the constraints under which it must operate. This document is intended for developers who want to implement this protocol.


### 1.2 Document Conventions
This document is based on the IEEE template for System Requirement Specification (SRS) document.


### 1.3 Intended Audience and Reading Suggestions

- Typical Users, such as developers, who want to use PCP to develop their chat applications.
- Students who are interested to discover how an extremely simple chat works.
- Small realities who need a way to talk between rooms withouth yelling and withouth losing control of their messages.


### 1.4 Project Scope
PotatoChatProtocol is a an extremely easy, portable and expandable protocol which can be used as base to develop a chat application.
The protocol defines all the constraints to create a functioning client capable of interacting with the server implemented in the project.


### 1.5 References
PCP's main page:
[https://jacopowolf.github.io/PotatoChatProtocol](https://jacopowolf.github.io/PotatoChatProtocol) \
PCP's Github page:
[https://github.com/JacopoWolf/PotatoChatProtocol](https://github.com/JacopoWolf/PotatoChatProtocol) \
PCP's Jitpack page:
[https://jitpack.io/#JacopoWolf/PotatoChatProtocol](https://jitpack.io/#JacopoWolf/PotatoChatProtocol)


## 2 Overall Description

### 2.1 Product Perspective
PCP is a protocol developed in order to have the possibility to implement an easy and portable chat application. It could be useful when a team needs to exchange information as quickly as possible.


### 2.2 Product Functions
Mainly PCP takes care of supplying the protocol standards in a clear and simple way, then it implements a server related to the last release version.

Protocol specifications:

- the structure of the packets that will be exchanged between the server and client-side applications.
- error and exception handling
- the procedures necessary for logging, disconnecting and exchanging messages between client and server.

Server side:

- build a server ready to manage connected clients and exchange messages between them.


### 2.3 Operating Environment
All modern operating systems that have a java virtual machine installed.


### 2.4 Design and Implementation Constraints
This project is developed with Java using NetBeans as IDE. The packages are organized according to the protocol versions. It uses a modular design where every feature is wrapped into a separate module and the modules depend on each other through well-written APIs.

This is the structure of the API:


![API](../img/server-architecture.svg)


### 2.5 User Documentation
There is a quick guide which explains how to initialize a PCPServer:
[https://github.com/JacopoWolf/PotatoChatProtocol/blob/master/docs/usage.md](https://github.com/JacopoWolf/PotatoChatProtocol/blob/master/docs/usage.md)

Moreover there is the Javadoc, hosted on JitPack:
[https://javadoc.jitpack.io/com/github/jacopowolf/potatochatprotocol/Min.a.3/javadoc/index.html](https://javadoc.jitpack.io/com/github/jacopowolf/potatochatprotocol/Min.a.3/javadoc/index.html)


### 2.6 Assumptions and Dependencies
PCP is developed in Java so to start-up a server the machine requires a Java version 8 (recommended) or higher and JDK version 8 (recommended) or higher.


## 3 External Interface Requirements
The server implemented by PCP only print the logs. No graphical interfaces have been implemented. Clients that rely on PCP will implement it.


### 3.2 Hardware Interfaces
The protocol is very light, if you implement a small chat you can start the server on most multi cores processors.
When the size increases accordingly the number of threads used will increase, this requires better performing processors.
The minimum amount of RAM required is 4 GB.


## 4 System Feature


### 4.1 Server
PCP offers the possibility to start a server which is able to manage the communication between several connected users.

A simple guide is avaible: [https://github.com/JacopoWolf/PotatoChatProtocol/blob/master/docs/usage.md](https://github.com/JacopoWolf/PotatoChatProtocol/blob/master/docs/usage.md)


## 5 Other Nonfunctional Requirements

### 5.1 Safety Requirements
To keep the server at the latest version, you need to update the software each time an update is released.
This allows you to keep the server updated to avoid bugs or problems.


### 5.2 Security Requirements
PCP does not have any specification from a security point of view, however, it is a good practice to install and start the server on suitable and limited access machines.
