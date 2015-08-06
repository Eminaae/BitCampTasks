package ba.bitcamp.lectures;

import java.io.BufferedWriter;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/**
 * To run this configuration first run the server one and than client. 
 * Chat client class connects user to the server, which can communicate continuously. GUI is used 
 * to have a field where user enters message, shows current messages. 
 * ChatClient class extends JFrame and implements two interfaces ActionListener and Runnable
 * @author emina.a
 *
 */
public class ChatClient implements ActionListener{

	//attributes declaration
	public static BufferedWriter writer;
	public static BufferedReader reader;
	private Socket clientSocket;
	private Thread t;
	private JFrame clientWindow;
	private JPanel panelClient;
	private JTextArea msgArea;
	private JTextField msgField;
	private JButton msgSend;
	private ExecutorService pool = Executors.newSingleThreadExecutor();

	/**
	 * Default constructor constructs chat client window and socket for a client
	 */
	public ChatClient() {
		clientWindow = new JFrame("ChatClient"); //frame for client
		panelClient = new JPanel(); // panel for client

		msgArea = new JTextArea();
		msgField = new JTextField();
		msgSend = new JButton("Send");
		msgSend.addActionListener(this); // adding an action listener to send button

		clientWindow.add(panelClient);
		panelClient.add(msgField);
		panelClient.add(msgSend);
		panelClient.add(msgArea);
		
		panelClient.setLayout(new BorderLayout());
		panelClient.add(msgArea, BorderLayout.NORTH); // adding text area to the panel
		panelClient.add(msgField, BorderLayout.CENTER);
		panelClient.add(msgSend, BorderLayout.SOUTH);
		
		clientWindow.setLocationRelativeTo(null);
		clientWindow.setSize(300, 400);
		clientWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientWindow.setResizable(false);
		clientWindow.setVisible(true);

		try {
			clientSocket = new Socket("10.0.82.27", 6815); //Socket for a client
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // reads input from input stream reader
			writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())); //reads output from output stream
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		//Thread will listen continuously
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(;;){
					String line;//blocking input output...will not let code to continue 
					try {
						line = reader.readLine();
						msgArea.append(line + "\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
/**
 * ActionPerformed method will be called after clicking on send button.
 * The value of text field will be written in buffered writer, appended to the string and at the
 * end text field will be cleaned.
 */
	
	@Override
	public void actionPerformed(ActionEvent e) {
		pool.submit(new Runnable(){
			@Override
			public void run() {
				try {
					//msgArea.setVisible(true);
					writer.write(msgArea.getText());
					msgArea.setText("");
					
					writer.newLine();
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) throws IOException {
		ChatClient client = new ChatClient(); //instantiate a chat client
		
	}
}
