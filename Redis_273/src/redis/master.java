package redis;

import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.hash.HashFunction;
import com.google.common.io.*;

import redis.clients.jedis.Jedis;

public class master {

	public static void main(String[] args) throws Exception {
		/*   HashFunction hashFunction;
		   int numberOfReplicas;
		    SortedMap<Integer, T> circle = new TreeMap<Integer, T>();
		 */

		Scanner sc = new Scanner (System.in);
		Jedis a = new Jedis("127.0.0.1",6376);
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
