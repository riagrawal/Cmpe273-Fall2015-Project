import redis
import sys
import hashing
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

def select_cluster(path,num_of_cluster,key):
    workbook = xlrd.open_workbook(path)
    address = [] 
    hash = int(key) % int(num_of_cluster)
    print "\n The cluster selected is : ", hash+1
    for sheet in workbook.sheets():
        for row in range(sheet.nrows):
            for column in range(sheet.ncols):
                if (row == hash):
                	address.append(sheet.cell(row,column).value)
    return address               
            
def countOfCluster(path):
	workbook = xlrd.open_workbook(path)
    #Total no of clusters
	for sheet in workbook.sheets():
		num=sheet.nrows
    	return num

def success(ip):
	print "\nThe connection to Redis Server at port " + str(ip) +" established \n"

if __name__ == "__main__":
    ring = hashring.ConsitentHashing()
    for i in range(100):
        ring.add_node(6379+i*10)

    ring.add_node(6380)
    ring.add_node(7212)
    ring.add_node(8020)

    print " Scenario 1 "

    print " HashRing for key=12341234 - hash=", ring.get_key("12341234") , "server=" ,ring.get_node("12341234")
    print " HashRing for key=crap - hash=", ring.get_key("crap") ,  ring.get_node("crap")
    print " HashRing for ajsvlaskd : ", ring.get_key("ajsvlaskd") ,  ring.get_node("ajsvlaskd")
    print " HashRing for earth : ", ring.get_key("earth") ,  ring.get_node("earth")
    print " HashRing for crap : ", ring.get_key("crap") ,  ring.get_node("crap")


    


	
	
