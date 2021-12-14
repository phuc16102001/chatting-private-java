# chatting-private-java

REQ: login-<username>-<password>
RES: login-<status>

REQ: signup-<username>-<password>
RES: signup-<status>

REQ: online
RES: online-<user1>-<user2>-...-<userN>

REQ: send-<toUser>-<message>
RES: None
OTHERS: send-<fromUser>-<message>

REQ: logout
RES: None

REQ: quit
RES: None

REQ: fileInfo-<toUser>-<length>
RES: fileRes-<status>-<length> (accept/deny)
REQ: <bits>