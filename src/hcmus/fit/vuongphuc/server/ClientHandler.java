/**
 * @package hcmus.fit.vuongphuc.server
 * @author VuongPhuc
 *
 * Dec. 9, 2021 - 3:59:27 a.m.
 * @since 2021
 * @version
 */
package hcmus.fit.vuongphuc.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

/**
 * Description:
 *
 * @author VuongPhuc
 * @see 
 */
public class ClientHandler implements Runnable {

	private final String DELIMITER = "-";
	
	Logs context = null;
	JTextArea txtLog = null;
	
	Socket socket = null;
	BufferedReader reader = null;
	BufferedWriter writer = null;
	
	Socket partner = null;
	String username = null;
	
	public String getUsername() {
		return this.username;
	}
	
	public ClientHandler(Logs context, Socket socket) throws IOException {
		this.context = context;
		this.txtLog = context.getTxtLog();
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	
		txtLog.append(String.format("Talking to client: [%s]\n",socket.getPort()));
	}
	
	private void sendClient(String tag, List<String> listMessage) {
		String sendMessage = tag;
		for (String message:listMessage) {
			sendMessage += DELIMITER + message;
		}
		try {
			writer.write(sendMessage);
			writer.newLine();
			writer.flush();
			txtLog.append(String.format("Send [%s]-[%s]\n",socket.getPort(),sendMessage));
		} catch (IOException e) {
			txtLog.append(String.format("Fail to send to client [%s]\n",socket.getPort()));
			e.printStackTrace();
		}
	}
	
	private void sendClient(String tag, String message) {
		List<String> messages = new ArrayList<>();
		messages.add(message);
		this.sendClient(tag, messages);
	}
	
	private void login(String username, String password) {
		String tag = "login";
		try {
			if (FileHandler.login(username,password)) {
				sendClient(tag,"success");
				this.context.login(username, this);
			} else {
				sendClient(tag,"fail");
			}
		} catch (IOException e) {
			sendClient(tag,"fail");
			e.printStackTrace();
		}
	}
	
	private void signup(String username, String password) {
		String tag = "signup";
		try {
			if (FileHandler.signup(username,password)) {
				sendClient(tag,"success");
			} else {
				sendClient(tag,"fail");
			}
		} catch (IOException e) {
			sendClient(tag,"fail");
			e.printStackTrace();
		}
	}
	
	private void getOnline() { 
		String tag = "online";
		sendClient(tag, context.getOnline());
	}
	
	private void processArg(String[] args) {
		String route = args[0];
		if (route.equalsIgnoreCase("login")) {
			String username = args[1];
			String password = args[2];
			login(username,password);
		}
		else if (route.equalsIgnoreCase("signup")) {
			String username = args[1];
			String password = args[2];		
			signup(username,password);
		}
		else if (route.equalsIgnoreCase("online")) {
			getOnline();
		}
		else if (route.equalsIgnoreCase("begin")) {
			
		} 
		else if (route.equalsIgnoreCase("send")) {
			
		}
	}
	
	@Override
	public void run() {
		try {
			do {
				String received = reader.readLine();
				txtLog.append(String.format("Receive [%s]-[%s]\n",socket.getPort(),received));					
				
				if (received.equalsIgnoreCase("quit")) {
					reader.close();
					writer.close();
					socket.close();
					context.logout(username);
					break;
				} else {
					String[] args = received.split(DELIMITER,-1);
					this.processArg(args);
				}
			} while (true);
		} catch (Exception e) {
			context.logout(username);
			e.printStackTrace();
		}
	}
}