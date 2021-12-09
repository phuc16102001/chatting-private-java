/**
 * @package hcmus.fit.vuongphuc.server
 * @author VuongPhuc
 *
 * Dec. 9, 2021 - 3:26:18 a.m.
 * @since 2021
 * @version
 */
package hcmus.fit.vuongphuc.server;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Description:
 *
 * @author VuongPhuc
 * @see 
 */
public class Logs extends JFrame {

	JTextArea txtLog = new JTextArea(10,50);
	ServerSocket socket = null;
	HashMap<String, ClientHandler> loginList = new HashMap<>();
	
	private class CreateSocketListener implements Runnable {
		
		Logs context = null;
		
		public CreateSocketListener(Logs context) {
			this.context = context;
		}
		
		@Override
		public void run() {
			try {
				txtLog.append("Creating socket...\n");
				socket = new ServerSocket(3200);
				do {
					txtLog.append("Listen on port 3200...\n");
					Socket clientSocket = socket.accept();
					
					Thread t = new Thread(new ClientHandler(context, clientSocket));
					t.start();
				} while (true);
			} catch (IOException e) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	}
	
	public void login(String username, ClientHandler client) {
		loginList.put(username, client);
	}
	
	public void logout(String username) {
		if (username!=null) {			
			loginList.remove(username);
		}
	}
	
	public List<String> getOnline() {
		Set<String> userList = loginList.keySet();
		List<String> result = new ArrayList<>();
		result.addAll(userList);
		return result;
	}
	
	public JTextArea getTxtLog() {
		return this.txtLog;
	}
	
	private JScrollPane createLogPanel() {
		JScrollPane panel = new JScrollPane(txtLog);
		
		panel.setBorder(new CompoundBorder(new TitledBorder("Logs"),new EmptyBorder(2,2,2,2)));
		txtLog.setEditable(false);
		
		return panel;
	}
	
	public Logs() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setTitle("Server logs");
		this.add(createLogPanel(), BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
		
		Thread t = new Thread(new CreateSocketListener(this));
		t.start();		
	}
}
