package redis;


import com.google.common.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Scanner;
import java.util.SortedMap;

import com.google.common.hash.HashFunction;

import redis.clients.jedis.Jedis;

public class masterslave2 {

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		/*   final HashFunction hashf;
		    final int numberOfReplicas;
		    final SortedMap<Integer, T> circle =  new TreeMap<Integer, T>();
*/
		Scanner sc = new Scanner (System.in);
		Jedis master = new Jedis("127.0.0.1",6386);
		Jedis slave = new Jedis("127.0.0.1",6385);
		
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
				master.set(st1, st2);
				break;
				
			case 2:
				Scanner sc2 = new Scanner (System.in);
				System.out.print("Enter key: ");
				String st3 = sc2.nextLine(); 
				System.out.println("Stored string in redis:: "+ slave.get(st3));
				
			}
			}while(ch!=3);
		
	}
	
}
