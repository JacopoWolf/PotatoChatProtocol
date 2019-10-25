<h1>
Potato Chat Protocol<br>
PCP-Minimal
</h1>

![](https://img.shields.io/badge/Status-discussion-red?style=flat-square)
![](https://img.shields.io/badge/Latest%20version-Min.0-informational?style=flat-square)



---

1. [Specifics](#specifics)
2. [0x - messages](#0x---messages)
   1. [00 - user to user](#00---user-to-user)
   2. [05 - user to chat](#05---user-to-chat)
3. [1x - client status](#1x---client-status)
   1. [10 - registration](#10---registration)
   2. [11 - disconnection](#11---disconnection)
      1. [client](#client)
      2. [from server](#from-server)
4. [2x - acknowledgments](#2x---acknowledgments)
   1. [21 - registration hack](#21---registration-hack)
5. [5x - control messages](#5x---control-messages)
   1. [50 - group users list](#50---group-users-list)
6. [255 - errors](#255---errors)

---


## Specifics

- The protocol uses a TCP connection
- Package maximum lenght is 2048 bytes
- Text encoding is UTF-8

## 0x - messages

### 00 - user to user
once connected to the server, the user will have to know the other user's name to directly chat with him.

If message lenght is greater than [ 2016 - ( source lenght + destination lenght ) ] it means the message has been splitted between multiple packages.
Last package is notified with a lenght lesser than 2016.
If the content results in a multiple of this number, an empty package will be sent to notify end of transmission.

<table style="width:100%">
   <tr>
      <th> . </th>
      <th> 8 </th>
      <th> 16 </th>
      <th> 24 </th>
      <th> 32 </th>
      <th> [...] </th>
   </tr>

   <tr>
      <td colspan="1"> opcode (00) </td>
      <td colspan="1"> source alias lenght </td>
      <td colspan="1"> destination alias lenght </td>
      <td colspan="2"> message lenght </td>
   </tr>
   <tr>
      <td colspan="10"> source alias  </td>
   </tr>
   <tr>  
      <td colspan="10"> destination alias </td>
   </tr>
   <tr>
      <td colspan="10"> message </td>
   </tr>
</table>


### 05 - user to chat
The server has already server a list of all the users in the chat.

If message lenght is greater than 2016 it means the message has been splitted between multiple packages.
Last package is notified with a lenght lesser than 2016.
If the content results in a multiple of this number, an empty package will be sent to notify end of transmission.


<table>
  <tr>
      <th> . </th>
      <th> 8 </th>
      <th> 16 </th>
      <th> 24 </th>
      <th> 32 </th>
      <th> [...] </th>
  </tr>
  <tr>
      <td colspan="1"> opcode (05) </td>
      <td colspan="2"> message lenght </td>
      <td colspan="2"> sender id </td>
  </tr>
  <tr>
      <td colspan="6"> message </td>
  </tr>
</table>


## 1x - client status

### 10 - registration

The first package sent to the local superclient-server by the client is a registration package.
This package tells to the server the user's alias ( nickname ), used to uniquily identify a user in a domain.
Alias lenght is min 6 to max 32 characters.

There are 2 registration types in PTP-Min
- 0
  <br>request to connect to the server. Ready to send and recieve messages from other users.
  Answered with a [reg ack](#21---registration-hack)
- 1
  <br>request to connect to the server's general room.
  Answered with a [users list]()



<table>
  <tr>
      <th> . </th>
      <th> 8 </th>
      <th> 16 </th>
      <th> [...] </th>
  </tr>

  <tr>
      <td colspan="1"> opcode ( 10 ) </td>
      <td colspan="1"> type </td>
      <td colspan="1"> alias lenght </td>
  </tr>
  <tr>  
      <td colspan="5"> alias  </td>
  </tr>
</table>



### 11 - disconnection

Disconnection can happen both from client and from server.


#### client

Sent when the client wants to disconnect from the server.


<table>
  <tr>
      <th> . </th>
      <th> 8 </th>
      <th> 16 </th>
      <th> [...] </th>
  </tr>

  <tr>
      <td colspan="1"> opcode ( 11 )</td>
      <td colspan="1"> type</td>
      <td colspan="1"> alias lenght</td>
  </tr>
  <tr>  
      <td colspan="10"> alias </td>
  </tr>
</table>


#### from server

sent by the server with the following possible opcodes:
- 0 no reason
- 1 timeot
  <br>This package is sent after 15 minutes client inactivity.
- 2 server offline



<table>
  <tr>
      <th> . </th>
      <th> 8 </th>
  </tr>

  <tr>
      <td colspan="1"> opcode ( 11 ) </td>
      <td colspan="1"> reason </td>
  </tr>
</table>


---


## 2x - acknowledgments

Those packages are sent by the server to acknowledge client actions.

### 21 - registration hack
sent after a user has connected with a 0 connection type

<table>
  <tr>
      <th> . </th>
      <th> 8 </th>
      <th> 16 </th>
      <th> [...] </th>
  </tr>

  <tr>
      <td colspan="1"> opcode ( 20 ) </td>
      <td colspan="1"> ackCode ( 1 ) </td>
      <td colspan="1"> alias lenght </td>
  </tr>
  <tr>
      <td colspan="10"> alias </td>
  </tr>
</table>


## 5x - control messages

### 50 - group users list
When connecting to a group, instead of wasting time in sending the user alias for every message, the server will assign a 16bit intger **ID** to every client.
To allow name solving for clients, the group user list is sent.

If message lenght is greater than 2024 it means the message has been splitted between multiple packages.
Last package is notified with a lenght lesser than 2024.
If the content results in a multiple of this number, an empty package will be sent to notify end of transmission.

The names and IDs are sent in two plaintext JSON lists of the same lenght.
One for the names, and one for the IDs. 

Like in the following example:


```json
{"aliases":["ALIAS1","ALIAS2","ALIAS3"],"ids":[12345,23456,34567]}
```

<table>
  <tr>
      <th> . </th>
      <th> 8 </th>
      <th> 16 </th>
      <th> 24 </th>
      <th> [...] </th>
  </tr>

  <tr>
      <td colspan="1"> opcode ( 50 ) </td>
      <td colspan="1"> list lenght </td>
      <td colspan="2"> content lenght </td>
  </tr>
  <tr>  
      <td colspan="10"> json content </td>
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

**200** - chat denied

You have been denied the access to a chat. Can be sent in a cient to client connection.

**202** - maximum clients reached

Server cannot answer your request because the maximum amount of users has been reached.

**255** - unspecified exception

An error has occured, in this extreme case the server will probably not answer correctly anymore.


<table>
  <tr>
      <th> . </th>
      <th> 8 </th>
  </tr>

  <tr>
      <td> opcode ( 255 ) </td>
      <td> error code </td>
  </tr>
</table>
