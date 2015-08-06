package ba.bitcamp.exercises;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Example {

	private static Integer primeNum = 1;
	private static LinkedBlockingQueue<Task> queue;
	private static ArrayList<Socket> list;
	private static Object lock = new Object(); //var koja sluzi samo za yakljucavanje privremeno
	//SETI
	//Search for extra Terrestrial Intelligence
	//RSA emcription
	//from 1 to 1000000
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(8000);
			int a = 1;
			int b = 1000;
			int workLoad = 1000;
			while(true){
				Socket client = server.accept();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static class Task{
		private int start;
		private int finish;
	}
	
	public static class Worker extends Thread{
		private Socket socket;
		private int a;
		private int b;
		
		public Worker(Socket socket, int a, int b){
			this.socket = socket;
			
		}
	}

}
