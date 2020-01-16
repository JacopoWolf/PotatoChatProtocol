<center>
<h1>
PCP-Minimal
</h1>
</center>

- [Overview](#overview)
  - [Description](#description)
  - [Connect to a server](#connect-to-a-server)
  - [Messaging](#messaging)
- [Packets](#packets)
  - [Specifics](#specifics)
    - [General](#general)
    - [Naming](#naming)
  - [0x - messages](#0x---messages)
    - [01 - user to user](#01---user-to-user)
    - [05 - user to chat](#05---user-to-chat)
  - [1x - client status](#1x---client-status)
    - [10 - registration](#10---registration)
    - [11 - disconnection](#11---disconnection)
      - [client](#client)
      - [server](#server)
    - [18 - change of alias](#18---change-of-alias)
  - [2x to 5x - control messages](#2x-to-5x---control-messages)
    - [20 - registration ack](#20---registration-ack)
    - [50 - group users list request](#50---group-users-list-request)
    - [51 - group users list](#51---group-users-list)
  - [255 - errors](#255---errors)

---

# Overview

## Description
The PCP-Minimal version of the PCP protocol is its stupid version, implementing an extremely easy, unprotected, channel-based and volatile LAN chat.

It in fact does not have any encryption system, except for the id system, which is used as a base level to avoid client impersonation;
all packets are sent in plain text over the network, and ways to manage logs, chat cronology and statuses are not planned.

## Connect to a server
First things first, the client needs to register to the server and have it acknowledge its existence.

The client will have to directly know the IP address of the server or have it solved by a DNS, and connect to the port **53101**.

Then it will proceed to choose an alias (nickname) used to uniquely identify an user from another and send a [registration request](#10---registration), then await a [registration ack](#20---registration-hack) from the server or
an [alias already in use](#255---errors) error (error code 100).

The registration ack will contain it's unique ID, a basic security measure because not having it would've been criminous.

At this point the PCP connection is established.

## Messaging
Once connected, the clients will simply send messages to the server, and the server, knowing all other clients, will deliver the message.

Messages can also be sent to a general group named "general", wich is essentially a public chat room.
No other chat rooms are available.

A client can be connected to the server and recieve just private messages, or connect to the server and also chat in the general room.

When the clients connects to the public room it will recieve an additional package, after the [registration ack](#20---registration-hack), containing the [online users list](#51---group-users-list), withouth having the client request it. Those are the users online in the current chatroom.

Now clients can freely send messages between them as long as they remain connected to the server.

---

# Packets

## Specifics

### General

- The protocol uses a **TCP** connection
- Package maximum lenght is **2048 bytes**
- Text encoding is **ISO/IEC-8859-1**
- Text must not contain an all to 0 character

### Naming

- Names *must not* contain spaces
- Alias lenght is min **6** to max **32** characters.
- Topics lenght is min **3** to max **64** characters


## 0x - messages
There are two types of messages:

- User to user
- User to group

### 01 - user to user
Once connected to the server, the user will have to know the other user's alias to directly chat with him.

It also sends it's unique id sent when initializing the connection.

If message lenght is greater than [ 2043 - alias lenght ] it means the message has been splitted between multiple packages.
There are two ways to notify the last packet:

- Message lenght lesser than [2043 - alias lenght].
- If the content results in a multiple of this number, a packet with the message field empty.


<table>
   <tr>
     <th colspan="4"> header </th>
     <th colspan="2"> payload </th>
  </tr>
   <tr>
        <th> 1 byte </th>
        <th> 2 bytes </th>
        <th> string </th>
        <th> 1 byte </th>
        <th> string </th>
        <th> 1 byte </th>
   </tr>
   <tr>
      <td> opcode (01) </td>
      <td> sender id</td>
      <td> destination alias </td>
      <td> 0 </td>
      <td> message </td>
      <td> 0 </td>
    </tr>
</table>
<br>

The destination client will then recieve a package with the destination alias changed to the source alias and no id.

<table>
   <tr>
      <th colspan="3"> header </th>
      <th colspan="2"> payload </th>
   </tr>
   <tr>
      <th> 1 byte </th>
      <th> string </th>
      <th> 1 byte </th>
      <th> string </th>
      <th> 1 byte </th>
   </tr>
   <tr>
      <td> opcode (01) </td>
      <td> source alias </td>
      <td> 0 </td>
      <td> message </td>
      <td> 0 </td>
    </tr>
</table>


### 05 - user to chat
The user sends this message to the chat room it's last connected to.

If message lenght is greater than [ 2044 ] it means the message has been splitted between multiple packages.
There are two ways to notify the last packet:

- Message lenght lesser than 2044.
- If the content results in a multiple of this number, a packet with the message field empty.


<table>
    <tr>
      <th colspan="2"> header </th>
      <th colspan="2"> payload </th>
    </tr>
    <tr>
        <th> 1 byte </th>
        <th> 2 bytes </th>
        <th> string </th>
        <th> 1 byte </th>
    </tr>
    <tr>
        <td > opcode (05) </td>
        <td > id </td>
        <td > message </td>
        <td > 0 </td>
    </tr>
</table>

The destination client will then recieve a package with the destination alias changed to the source alias and no id.

<table>
   <tr>
      <th colspan="3"> header </th>
      <th colspan="2"> payload </th>
   </tr>
   <tr>
      <th> 1 byte </th>
      <th> string </th>
      <th> 1 byte </th>
      <th> string </th>
      <th> 1 byte </th>
   </tr>
   <tr>
      <td> opcode (05) </td>
      <td> source alias </td>
      <td> 0 </td>
      <td> message </td>
      <td> 0 </td>
    </tr>
</table>



## 1x - client status

### 10 - registration
The first package sent to the local superclient-server by the client is a registration package.
This package tells to the server the user's alias ( nickname ), used to uniquily identify a user in a domain, and the optional topic to be connected to.

If no topic is passed, then the server will assume a private conversations only connection.

Default topic to connecto to the general room is "general", other topics generate an error in this version of the protocol.


<table>
    <tr>
        <th> 1 byte </th>
        <th> 1 byte </th>
        <th> string </th>
        <th> 1 byte </th>
        <th> string </th>
        <th> 1 byte </th>
    </tr>
  <tr>
        <td> opcode ( 10 ) </td>
        <td> version ( 0 ) </td>
        <td> alias </td>
        <td> 0 </td>
        <td> topic (optional) </td>
        <td> 0 </td>
  </tr>  
</table>



### 11 - disconnection

Disconnection can happen both from client and from server.


#### client

Sent when the client wants to disconnect from the server.


<table>
  <tr>
      <th> 1 byte </th>
      <th> 2 bytes </th>
  </tr>
  <tr>
      <td> opcode ( 11 )</td>
      <td> id </td>
  </tr>
</table>


#### server

sent by the server with the following possible opcodes:

- 0 no reason
- 1 timeout
  <br>This package is sent after 15 minutes client inactivity.
- 2 server gone offline



<table>
  <tr>
      <th> 1 byte </th>
      <th> 1 byte </th>
  </tr>

  <tr>
      <td > opcode ( 11 ) </td>
      <td > reason </td>
  </tr>
</table>





### 18 - change of alias
A user might want to change its alias. This package allows to do that.


<table>
    <tr>
        <th> 1 byte </th>
        <th> 2 byte </th>
        <th> string </th>
        <th> 1 byte </th>
        <th> string </th>
        <th> 1 byte </th>
    </tr>
  <tr>
        <td> opcode ( 18 ) </td>
        <td> private id </td>
        <td> old alias </td>
        <td> 0 </td>
        <td> new alias </td>
        <td> 0 </td>
  </tr>  
</table>



## 2x to 5x - control messages

Those packages are sent by the server to acknowledge client actions.

### 20 - registration ack
sent after a user has requested a connection.
Assigns an id to the user and re-sends the alias to confirm it's correctness.

<table>
    <tr>
        <th> 1 byte </th>
        <th> 2 bytes </th>
        <th> string </th>
        <th> 1 byte </th>
    </tr>

  <tr>
      <td > opcode ( 20 ) </td>
      <td > assigned id </td>
      <td > alias confirmation </td>
      <td> 0 </td>
  </tr>
</table>


### 50 - group users list request
Sent to request the whole group users list by a client.

<table>
    <tr>
        <th> 1 byte </th>
        <th> 2 bytes </th>
    </tr>

  <tr>
      <td > opcode ( 50 ) </td>
      <td > assigned id </td>
  </tr>
</table>

### 51 - group users list
When connecting to a group, instead of wasting time in sending the user alias for every message, the server will assign a 16bit intger **ID** to every client.
To allow name solving for clients, the group user list is sent.

If message lenght is greater than 2024 it means the message has been splitted between multiple packages.
Last package is notified with a lenght lesser than 2024.
If the content results in a multiple of this number, an empty package will be sent to notify end of transmission.

The names are sended in a JSON list.
Like in the following example:

Types:

- 0 complete users list
- 1 joined user
- 2 disconnected user

```json
["ALIAS1","ALIAS2","ALIAS3"]
```

In the event that the type is 1 or 2, the json list will contain only one user, the one who has connected or disconnected.

  <table>
    <tr>
      <th colspan="3"> header </th>
      <th colspan="2"> payload </th>
    </tr>
    <tr>
        <th> 1 byte </th>
        <th> 1 byte </th>
        <th> 1 byte </th>
        <th> string </th>
        <th> 1 byte </th>
    </tr>

  <tr>
      <td > opcode ( 51 ) </td>
      <td > type  </td>
      <td > list length </td>
      <td > json content </td>
      <td > 0 </td>
  </tr>
</table>


## 255 - errors

An error package is used to notify of errors.
The Error Type field contains the reason opcode:

Error packages have the same format.


**000** - malformed package

An error has occured while attempting to interpret the package.

**100** - alias already in use

The given alias is aready in use by another client.

**101** - unvalid alias

The given alias breaks alias constrictions.

**102** - invalid room name

given room name is invalid

**200** - chat denied

You have been denied the access to a chat. Can be sent in a client to client connection.

**202** - maximum clients reached

Server cannot answer your request because the maximum amount of users has been reached.

**254** - server exploded

because of meme reasons

**255** - unspecified exception

An error has occured, in this extreme case the server will probably not answer correctly anymore.


<table>
  <tr>
      <th> 1 byte </th>
      <th> 1 byte </th>
  </tr>

  <tr>
      <td> opcode ( 255 ) </td>
      <td> error code </td>
  </tr>
</table>
