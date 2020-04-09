# Developer Documentation
Last Updated : April 9th, 2020

**NOTE : The Server is currently being re-written into a Web Application from a JavaFX Application to make development faster and easier. Certain information about the Server may be changed in future releases.**

**However, The Client will always stay as a JavaFX Project because its faster than Web, especially on low-powered cheap SBCs like the Raspberry Pi and also Android + iOS.**

* Networking (Between Server and Client)
* Exclusive Comamnds
* User Actions
    - Hotkeys
    - Twitter
    - Launch App
    - Launch Website
    - OBS Studio
    - Scripts
    - Basic GPIO Handling **(Raspberry Pi Only)**
    - Custom User Actions (WIP)
        
##Networking (Between Server and Client)
The StreamPi [Server](https://github.com/dubbadhar/streampi_server) and the StreamPi [Client](https://github.com/dubbadhar/streampi_client) communicate via standard Sockets. The communication between them is unencrypted and is done via normal strings to attain minimal lattency.

They communicate through specific commands, which are all shown discussed further below

A sample Command Structure: 

`COMMAND_NAME::COMMAND_VALUE::`

`::` is used as a separator character between commands and their values. Command values itself can have differnet separators to obtain different values.

Since no personal or critical information is sent through Sockets, the entire communication is kept unencrypted and simple.

##Exclusive Comamnds
These are some commands which are required to send to/receive from the client to share information like action names, icons, etc.

### From Client to Server

####action_icon

Structure : `action_icon::<icon name>::<Base64 version of image>::`

Sent when the Server queries the Client about the available actions and their icons.


####client_actions

Structure : `client_actions::<n No Of Actions>::<Action Object>::<Action Object>__...n::`

Each `<Action Object>` contains the following:
`<UniqueID>__<Casual Name>__<Action Command Name>__<Action Command Value>__<Name of Action Icon>__<Action Row No>__<Action Col No>__<Action Layer No.>::`


####client_details

Structure : `client_details::<Client IP>::<Client Nickname>::<Client Display Width>::<Client Display Height>::<Max no of actions per row>::<Each Action Size in Pixels>::<Padding between each action>::<Client Version>::`

Sent at the beginning when connection is first established.

####client_quit

Structure : `client_quit::`

Sent when the Client wants to quit and end connection to the Server. The server accordingly closes socket connection to the Client.

###From Server to Client

#### client_actions_icons_get
Structure : `client_actions_icons_get::`

Asks client to send all action icons

#### get_actions

Structure : `get_actions::`

Asks Client to send all action names and Action Objects to the Client

#### update_icon

Structure : `update_icon::<Icon Name>::<Icon in Base64>::`

Sends new icon to client to update

#### actions_update

Structure : `actions_update::<n No. Of Actions>::<Action Objects>::....n::`

Sends all new update actions to the client

#### delete_action

Structure : `delete_action::<Action Unique ID>::<Action Icon Name>::`

Requests the Client to delete a specific Action

####client_action_size_padding_update

Structure : `client_action_size_padding_update::<Each Action Size in Pixels>::<Padding between each Action in Pixels>::`

Updates each Action Size in Pixels and also the Padding between each action.

####action_success_response
Structure : `action_success_response::<Action Unique ID>::<Status - '1' for Success, '2' for Failure>`

Sends the Success status whether the action clicked on the client was executed successfully or not.

####client_details
Structure : `client_details::`

Asks Client to return its details
## User Actions
Similar to the StreamDeck, actions are well, just Actions. They have an icon, a specific name and values. 

All actions are stored in the Client itself.

####Hotkeys
The Server uses standard JavaFX Robot class to run Hotkeys.

A standard Hotkey Order from Client : `hotkey::KEY_VALUE<>KEY_VALUE::`

####Twitter
The [Twitter4J](http://twitter4j.org/en/) Library is used to communicate with Twitter in the Server

Example Action from Client : `tweet::<MESSAGE>`

####Script

Example : `script::cmd<>D:\script.bat::`

