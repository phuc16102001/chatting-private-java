/**
 * @package hcmus.fit.vuongphuc.client
 * @author VuongPhuc
 *
 * Dec. 9, 2021 - 2:22:19 p.m.
 * @since 2021
 * @version
 */
package hcmus.fit.vuongphuc.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import hcmus.fit.vuongphuc.constant.Tag;

/**
 * Description:
 *
 * @author VuongPhuc
 * @see 
 */
public class SocketHandler {
	private Socket socket = null;
	private BufferedReader reader = null;
	private BufferedWriter writer = null;
	private Thread listenThread;
	
	private static SocketHandler instance = null;
	
	private SocketHandler() {}
	
	public Socket getSocket() {
		return socket;
	}
	
	public String readLine() throws IOException {
		return reader.readLine();
	}
	
	public String send(String message, boolean hasResponse) throws IOException {
		String response = null;
		
		writer.write(message);
		writer.newLine();
		writer.flush();
		
		if (hasResponse==true) {
			response = reader.readLine();
		} 
		
		return response;
	}
	
	public String send(String tag, String[] messages, boolean hasResponse) throws IOException{
		String response = null;

		String sendMessage = tag;
		for (String message:messages) {
			sendMessage += Tag.DELIMITER+message;
		}
		
		response = send(sendMessage, hasResponse);
		
		return response;
	}

	public void send(String tag, String...messages) throws IOException {
		this.send(tag, messages, false);
	}
	
	public void send(String tag, String message, boolean hasResponse) throws IOException {
		String[] messages = new String[] {message};
		this.send(tag, messages, hasResponse);
	}
	
	public void send(String tag, String message) throws IOException {
		String[] messages = new String[] {message};
		this.send(tag, messages);
	}
	
	public static SocketHandler getInstance() {
		if (instance==null) {
			instance = new SocketHandler();
		}
		return instance;
	}
	
	public void close() throws IOException {
		writer.close();
		reader.close();
		socket.close();
	}
	
	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//		this.socket.setSoTimeout(5000);
	}
}
