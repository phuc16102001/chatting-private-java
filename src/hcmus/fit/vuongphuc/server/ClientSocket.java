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

import hcmus.fit.vuongphuc.constant.Tag;

/**
 * Description:
 *
 * @author VuongPhuc
 * @see 
 */
public class ClientSocket implements Runnable {
	
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
	
	public ClientSocket(Logs context, Socket socket) throws IOException {
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
			sendMessage += Tag.DELIMITER + message;
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
	
	private void sendClient(String tag, String...strings) {
		List<String> listMessage = new ArrayList<String>();
		for (String s:strings) {
			listMessage.add(s);
		}
		sendClient(tag, listMessage);
	}
	
	private void sendClient(String tag, String message) {
		List<String> messages = new ArrayList<>();
		messages.add(message);
		this.sendClient(tag, messages);
	}
	
	private void login(String username, String password) {
		String tag = Tag.LOGIN;
		try {
			if (FileHandler.login(username,password)) {
				sendClient(tag,Tag.SUCCESS);
				this.username = username;
				this.context.login(username, this);
			} else {
				sendClient(tag,Tag.FAIL);
			}
		} catch (IOException e) {
			sendClient(tag,Tag.FAIL);
			e.printStackTrace();
		}
	}
	
	private void signup(String username, String password) {
		String tag = Tag.SIGNUP;
		try {
			if (FileHandler.signup(username,password)) {
				sendClient(tag,Tag.SUCCESS);
			} else {
				sendClient(tag,Tag.FAIL);
			}
		} catch (IOException e) {
			sendClient(tag,Tag.FAIL);
			e.printStackTrace();
		}
	}
	
	private void getOnline() { 
		String tag = Tag.LIST_ONLINE;
		sendClient(tag, context.getOnline());
	}
	
	private boolean processArg(String[] args) {
		String route = args[0];
		if (route.equalsIgnoreCase(Tag.LOGIN)) {
			String username = args[1];
			String password = args[2];
			login(username,password);
		}
		else if (route.equalsIgnoreCase(Tag.SIGNUP)) {
			String username = args[1];
			String password = args[2];		
			signup(username,password);
		}
		else if (route.equalsIgnoreCase(Tag.LIST_ONLINE)) {
			getOnline();
		}
		else if (route.equalsIgnoreCase(Tag.SEND_TEXT)) {
			String username = args[1];
			String message = args[2];
			context.loginList.get(username).sendClient(Tag.SEND_TEXT, this.username, message);
		}
		else if (route.equalsIgnoreCase(Tag.LOGOUT)) {
			context.logout(username);
		}
		else if (route.equalsIgnoreCase(Tag.QUIT)) {
			return true;
		} 
		else if (route.equalsIgnoreCase(Tag.SEND_FILE)) {
			String username = args[1];
			int length = Integer.valueOf(args[2]);
			
		}
		return false;
	}
	
	@Override
	public void run() {
		try {
			do {
				String received = reader.readLine();
				txtLog.append(String.format("Receive [%s]-[%s]\n",socket.getPort(),received));					
		
				String[] args = received.split(Tag.DELIMITER,-1);
				boolean stop = this.processArg(args);
				if (stop) {
					break;
				}
			} while (true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				context.logout(username);
				reader.close();
				writer.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}