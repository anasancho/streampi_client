# Developer Documentation
Last Updated : February 21st, 2020

**NOTE : The Server is currently being re-written into a Web Application from a JavaFX Application to make development faster and easier. Certain information about the Server may be changed in future releases.**

**However, The Client will always stay as a JavaFX Project because its faster than Web, especially on low-powered cheap SBCs like the Raspberry Pi.**

1. StreamPi Communication Algorithm
    * Networking (Between Server and Client)
    * Actions
        - Hotkeys
        - Twitter
        - Launch App
        - Launch Website
        - OBS Studio
        - Scripts
        - Basic GPIO Handling **(Raspberry Pi Only)**
        - Custom User Actions (WIP)
2. Commands
    * Client Exclusive 
    * Server Exclusive
    * Shared
    
### StreamPi Communication Algorithm
####Networking (Between Server and Client)
The StreamPi [Server](https://github.com/dubbadhar/streampi_server) and the StreamPi [Client](https://github.com/dubbadhar/streampi_client) communicate via standard Sockets. The communication between them is unencrypted and is done via normal strings to attain minimal lattency.

They communicate through specific commands, which are all shown discussed further below

A sample Command Structure: 

`::COMMAND_NAME::COMMAND_VALUE::`

`::` is used as a separator character between commands and their values. Command values itself can have differnet separators to obtain different values.

Since no personal or critical information is sent through Sockets, the entire communication is kept unencrypted and simple.

####Actions
Similar to the StreamDeck, actions are well, just Actions. They have an icon, a specific name and values. 

#####Hotkeys
The Server uses standard JavaFX Robot class to run Hotkeys.

A standard Hotkey Order from Client : `::hotkey::KEY_VALUE<>KEY_VALUE::`

The Client actually stores 