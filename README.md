# Cmpe273-Fall2015-Project

#####hashring.py##########
This module handles the creation, deletion of nodes in the hash ring

####redis_server.py#######
-This module interacts with the user to take inputs including key value pair
-This module imports hashring  , so that it can use all tha methods in hashring.py to get hash key for 
 inputted key from user and assigning node to it
-We have added the functionality of creation of new node after a certain number of transactions performed.
 In our example we have considered the number to be 4.
-After creation of new node , new values as well as the existing values will be re-arranged accordingly among all the nodes including the new ones.
-Hash values will always be consistent for particular key as the number of server scale.






