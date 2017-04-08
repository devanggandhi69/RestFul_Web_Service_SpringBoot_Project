# RestFul_Web_Service_SpringBoot_Project
Created the backend for a mini messaging service, inspired by Twitter.

RESTful API where all endpoints require HTTP Basic authentication and generate output in JSON format. Implement the the following basic functionality:

`An endpoint to read the message list for the current user (as identified by their HTTP Basic authentication credentials). Include messages they have sent and messages sent by users they follow. Support a “search=” parameter that can be used to further filter messages based on keyword.`

`Endpoints to get the list of people the user is following as well as the followers of the user.`

`An endpoint to start following another user.`

`An endpoint to unfollow another user.`

`An endpoint that returns the current user's "shortest distance" to some other user. The shortest distance is defined as the number of hops needed to reach a user through the users you are following (not through your followers; direction matters). For example, if you follow user B, your shortest distance to B is 1. If you do not follow user B, but you do follow user C who follows user B, your shortest distance to B is 2.` 


Used: Spring Boot, H2 in Memory SQL Database
---
http://localhost:8080/user/10

Method: Get

Description: 10 is current user id. This will return list of messages the user is following as well as follow 

---
http://localhost:8080/user/10?search=ut

Method: Get

Desciption: Support a “search=” parameter that can be used to further filter messages based on keyword.

---
http://localhost:8080/user/10/connections

Method: Get

Description: 10 is current user id. Get the list of people the user is following as well as the followers of the user.

---
http://localhost:8080/user/10/follow/2

Metho: Put

Description: 10 is current user id and 2 is another user id. So here 10 start following 2. 

---
http://localhost:8080/user/10/unfollow/2

Metho: Delete

Description: 10 is cirrent user id and 2 is another user id. So 10 unfollowing 2

---
http://localhost:8080/user/10/distance/6

Method: Get

Description: Return distance between 10 and 6.
