import redis
import xlrd
import sys


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
    print "Hash value for given input is ,", hash
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
	path = "C:\Users\Rii\Documents\cmpe 273\project\\test.xlsx"
	num_of_cluster =  countOfCluster(path)
	opt = "y"
	while(opt == "y"):
		key = raw_input("Enter the value of key : ")
		value = raw_input("Enter the value to be entered : ")
		ip_address = select_cluster(path,num_of_cluster,key)
		#try:
		POOL = redis.ConnectionPool(host=ip_address[0], port=ip_address[1], db=0)
		success(int(ip_address[1]))
		#except Exception as e:
		#	print "Connection to " +ip_address[0]+" not established!!!"
		setVariable(key,value)
		response = getVariable(key)
		print "value of key - " + key + " -  is " + response
		opt= raw_input("\nDo you want to continue ? y/n \n")
	
	
	
