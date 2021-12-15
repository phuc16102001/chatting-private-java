# Chatting Private Java

A chat application between n-n clients in which can transfer files and check for online status. User accounts are stored in the text file.

# Usage
To compile, you can run the `compile.bat` file:
```bat
./compile.bat
```

To run server/client, type in command line:
```bat
java -jar jar/<file_name>.jar
```
Where `<file_name>` is server/client

Or, run file `run-client.bat` and `run-server.bat`

# Architecture
The structure is divided into four packages:
- ui: contains a template dialog `MyDialog`
- constant: contains static constant String (include tags, delimiters)
- server: all files relative to server
- client: all files relative to client

# Payload
## Login

Client send: `login-<username>-<password>`

Server response:
- Login successful: `login-success`
- Login fail: `login-fail`

## Signup
Client send: `signup-<username>-<password>`

Server response:
- Signup successful: `signup-success`
- Signup fail: `signup-fail`

## Logout
Client send: `logout`

Server response: NONE

## Get online list
Client send: `online`

Server response: `online-<user1>-<user2>-...-<userN>`

## Send text message
Client send: `sendText-<targetUser>-<message>`

Server response: NONE

## Send file
The send file process including 4 sub-processes:
- Request with information: Receive a request from A, and send to B the same payload
    - Client: `fileInfo-<fromUser>-<toUser>-<fileLength>-<fileName>`
    - Where `<fileLength>` is number of bytes and `<fileName>` is the name of transfering file
- Response for the request:
    - Client: `fileResponse-<fromUser>-<toUser>-<answer>-<fileLength>-<fileName>`
    - Where `<answer>` can be `accept` or `deny`
- Notify ready:
    - Client: `sendFile-<fromUser>-<toUser>-<fileLength>-<fileName>`
- Send bytes: send a sequences of byte after notified

Take an example that `A` is transfer `hello.txt` to `B` whose size is `8 bytes`. Firstly, `A` send to server and transfered to `B`:
```
fileInfo-A-B-8-hello.txt
```
After that, `B` accept the request by:
```
fileResponse-B-A-8-hello.txt
```
Finally, `A` notifies and transfer bytes immediately.
```
sendFile-A-B-8-hello.txt
```

# Contribution
Project is developed by [phuc16102001](https://github.com/phuc16102001) that was a submission of lab 2 in Java Programming course. Please **do not copy** in any case.