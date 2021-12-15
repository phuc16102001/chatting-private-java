/**
 * @package hcmus.fit.vuongphuc.client
 * @author VuongPhuc
 *
 * Dec. 9, 2021 - 2:22:19 p.m.
 * @since 2021
 * @version
 */
package hcmus.fit.vuongphuc.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
	private DataInputStream reader = null;
	private DataOutputStream writer = null;
	
	private static SocketHandler instance = null;
	
	private SocketHandler() {}
	
	public DataInputStream getReader() {
		return reader;
	}
	
	public DataOutputStream getWriter() {
		return writer;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public String readLine() throws IOException {
		return reader.readUTF();
	}
	
	public String send(String message, boolean hasResponse) throws IOException {
		String response = null;
		
		writer.writeUTF(message);
		writer.flush();
		
		System.out.println(String.format("Write package: [%s,%s]",message,String.valueOf(hasResponse)));
		
		if (hasResponse==true) {
			response = reader.readUTF();
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
		this.reader = new DataInputStream(socket.getInputStream());
		this.writer = new DataOutputStream(socket.getOutputStream());
//		this.socket.setSoTimeout(5000);
	}
}
