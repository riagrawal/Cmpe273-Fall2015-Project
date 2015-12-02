package redis;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class slave {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner (System.in);
		Jedis a = new Jedis("127.0.0.1",6375);
		
		int ch;
		do{
		System.out.println("1.push");
		System.out.println("2.pop");
		System.out.println("3.end");
		System.out.print("Enter your choice: ");
		 ch = sc.nextInt();
		switch (ch) {
		
		case 1: 
			
			Scanner sc1 = new Scanner (System.in);
			System.out.print("Enter key: ");
			String st1 = sc1.nextLine(); 
			
		
			System.out.println("Enter Value: ");
			String st2 = sc1.nextLine(); 
			
			
			System.out.println("Data saved successfully....");
			a.set(st1, st2);
			break;
			
		case 2:
			Scanner sc2 = new Scanner (System.in);
			System.out.print("Enter key: ");
			String st3 = sc2.nextLine(); 
			System.out.println("Stored string in redis:: "+ a.get(st3));
			
		}
		}while(ch!=3);
		
	}
	
	
}
