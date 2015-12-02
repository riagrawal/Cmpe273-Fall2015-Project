import redis
import sys
import hashring

class ServerNode(object):
    """Represents a Server node."""
    
    def __init__(self, port):
        "Creates a new instance"
        self._port = port
    
    def getPort(self):
        "Returns the server port"
        return self._port

def getVariable(variable_name):
    my_server = redis.Redis(connection_pool=POOL)
    response = my_server.get(variable_name)
    return response

def setVariable(variable_name, variable_value):
    my_server = redis.Redis(connection_pool=POOL)
    my_server.set(variable_name, variable_value)


if __name__ == "__main__":
    ring = hashring.ConsitentHashing()
    hash_array =[]
    port_array =[]
    key_array = []

    ##Adding initial 3 nodes i.e. servers at ports 6379, 6381 and 6383
    ring.add_node(6379)
    ring.add_node(6381)
    ring.add_node(6383)
    new_node_port = 6385

    ##Initialize the counter to track the number of transactions, 
    ##if value of i reaches 4 , it will create a new node
    i= 1  
    ##Take input key and value from user until he/she selects "n" to not continue
    opt = "y"
    while(opt == "y"):
        
        key = raw_input("Enter the value of key : ")
        value = raw_input("Enter the value to be entered : ")
        
        key_array.append(key)

        ##This will generate the hash key for inputted key from user
        hash_key = ring.get_hash(key)
        hash_array.append(hash_key)

        ##This will select the port of the redis server to connect
        ports = ring.get_node_port(key)
        port_array.append(ports)

        ##This establishes connection to require redis server to store hash key value pair
        POOL = redis.ConnectionPool( host="127.0.0.1",port= ports , db=0)

        ##This sets the hash key value pair to connected redis instance
        setVariable(key,value)
        print "Key = ",key, " hash value = ",hash_key,"port = ",ports

        response = getVariable(key)
        print "value of key - " + key + " -  is " + response

        if(i==4):
            ##Add a new node as value of i is 4
            ring.add_node(new_node_port)
            print "\nNew node has been created at this point at port :",new_node_port,"\n"

            ##Prints the summary of all the entries up until now
            print "\nSummary up until now:\n"
            for j in range(len(hash_array)):
                print "Key = ",key_array[j], " hash value = ",hash_array[j],"port = ",port_array[j]
            key_array =[]
            hash_array=[]
            port_array=[]

            ##Sets new value of port for next node creation
            new_node_port = new_node_port+2
            ##Re initialize the counter for next node creation after 4 iteration
            i=0
        i = i+1
        opt= raw_input("\nDo you want to continue ? y/n \n")

    for j in range(len(hash_array)):
        print "Key = ",key_array[j], " hash value = ",hash_array[j],"port = ",port_array[j]



    


    


	
	
