<center><h1>Testing specifications</h1></center>

## Introduction

This document outlines the test plan for the PotatoChatProtocol. The testing activities discussed in this document will verify that the software is capable of manage a chat between different client developed by different team following the structure of the protocol. 

### Items and features tested

The testing routine will test the management of the users in the server, exchange of messages between a client and another, the structure of the packets, log in and log out by the users. The results of this testing procedure will enable the creators of this system to gauge project success.

### Approach

The overall method to this testing procedure is manual system testing using Junit library which implement testing tools.

Manual system testing will continue throughout the second and third iteration of the project. For each iteration, both old and newly implemented features will be tested. Adding new features or functionality can sometimes interfere with the functionality of old features and to ensure product/project success, all features implemented should function as intended throughout the life of the software.

### Item Pass/Fail Criteria

The minimum requirements for this software system were laid out project success criteria.  

Features that contain major defects will fail the testing procedure and will be turned over to the developer for investigation and revision.

## Test Deliverables

In addition to the Test Plan, other test deliverables include the Test Specification which outlines the specific test cases and expected results of each test, and Test reports which is comprised of Incidents, Defects and Changes.

### Testing tasks

The following list the testing deliverables and the activities required to produce the deliverable.

| Deliverables        | Activities                                                                                                                                                                                                                                  |
| :------------------ | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Test Plan           | Analyze Requirements for System Features, Determine TestableNon-Testable Features, Develop Approach/Method for testing, Determine Task and Estimate Efforts, Develop Schedule for Testing                                                   |
| Test specifications | Analyze Requirements, Define Test Cases for Testable Features                                                                                                                                                                               |
| Test reports        | Implement Test Cases as Outlined by the Test Specifications, Document Incidents and Defects, Determine Severity of Incidents and Defects, Determine Changes that Need to be Made to System, Document and Submit Change Request to Developer |

## Test cases

### Asynchronous management

|                     |                                                            |
| :------------------ | :--------------------------------------------------------- |
| **Test ID**         | test1                                                      |
| **Title**           | Asynchronous packets management                            |
| **Objective**       | Confirm that the server can handle packets asynchronously  |
| **Setup**           | Junit testing on Netbeans IDE                              |
| **Test Data**       | Packets automatically generated                            |
| **Test actions**    | Allows the server to receive sent packets and process them |
| **Expected Result** | Server ends proccess data succesfully                      |

### Login and logout

|                     |                                                                                                                  |
| :------------------ | :--------------------------------------------------------------------------------------------------------------- |
| **Test ID**         | test2                                                                                                            |
| **Title**           | Login and logout by users                                                                                        |
| **Objective**       | Confirm that the server can handle succesfully the communications which permit login and logout actions by users |
| **Setup**           | Junit testing on Netbeans IDE                                                                                    |
| **Test Data**       | User entitits automatically generated                                                                            |
| **Test actions**    | Simply log in and log out of a user                                                                              |
| **Expected Result** | Server handles these two operations without errors                                                               |

### Messages exchange

|                     |                                                                              |
| :------------------ | :--------------------------------------------------------------------------- |
| **Test ID**         | test3                                                                        |
| **Title**           | Management of communication between users                                    |
| **Objective**       | Confirm that the server can handle single or broadcast communcation          |
| **Setup**           | Manual, with one physical machine on which to run the server and two clients |
| **Test Data**       | Packets manually generated                                                   |
| **Test actions**    | Private exchange of messages and broadcast exchange of messages (in a room)  |
| **Expected Result** | Every users can send and see rights messages                                 |
