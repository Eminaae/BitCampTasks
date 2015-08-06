package ba.bitcamp.lectures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
	
	private static LinkedBlockingQueue<Client> clients;
	private static LinkedBlockingQueue<Message> msgs;
	private static ExecutorService pool = Executors.newFixedThreadPool(8);
	
	

	public static void main(String[] args) {
		clients = new LinkedBlockingQueue<Client>();
		msgs = new LinkedBlockingQueue<Message>();
		pool.submit(new Listener()); //it will listen ones and shut down, so we need to 
		pool.submit(new Listener());
		pool.submit(new Listener());
		pool.submit(new Listener());
		pool.submit(new Listener());
		pool.submit(new Sender());
		pool.submit(new Sender());
		pool.submit(new Sender());
		try {
			ServerSocket server = new ServerSocket(6815);
			//goes into infinite loop
			Socket client;
					
			while(true){
				client = server.accept();
				clients.add(new Client(client));
				new Thread(new Listener()).start();
				new Thread(new Sender()).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * listens clients messages
	 * @author emina.arapcic
	 *
	 */
	private static class Listener implements Runnable{
		@Override
		public void run() {
			//gets client from queue
			Client c = null;
			try {
				c = clients.take();
				
				BufferedReader reader = c.getReader();
				
				StringBuilder sb = new StringBuilder();
				try {
					//Thread.sleep(30000);
						while(reader.ready()){
						sb.append(reader.readLine()); //we add all we got from one client and add to sb
				
					//this works only for one client
					Message message = new Message(c.getId(), sb.toString()); //sb.toString is content
					msgs.add(message);
					 // now we have 5 threads listening
					}c = clients.take(); //if we call take an nothing is in queue thread waits
						
				} catch (IOException e) {
					e.printStackTrace();
				}clients.add(c);
			} catch (InterruptedException e) {
				clients.remove(c);
				e.printStackTrace();
			}
			pool.execute(this);
		}
		
		
	}
	
	/**
	 * Sends to all clients received msgs, 
	 * take msgs, goes thru all clients, sends msgs to all clients 
	 * @author emina.arapcic
	 *
	 */
	private static class Sender implements Runnable{

		@Override
		public void run() {
			try {
				Message m = msgs.take();
				Client[] clientArr = null;
				synchronized(clients){
					System.out.println("Listening" + clientArr.length);
					clientArr = new Client[clients.size()]; //check for toArray description, local copy, all clients should be in this array
					for(int i = 0; i < clientArr.length; i++){
						clientArr[i] = clients.take();
					}
				}
				
				for (int i = 0; i < clientArr.length; i++) {
					BufferedWriter writer = clientArr[i].getWriter();
					try {
						writer.write(m.getMessage());
						writer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}pool.execute(this);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
