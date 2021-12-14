/**
 * @package hcmus.fit.vuongphuc.client
 * @author VuongPhuc
 *
 * Dec. 14, 2021 - 5:07:58 p.m.
 * @since 2021
 * @version
 */
package hcmus.fit.vuongphuc.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import hcmus.fit.vuongphuc.constant.Tag;
import hcmus.fit.vuongphuc.ui.MyDialog;

/**
 * Description:
 *
 * @author VuongPhuc
 * @see 
 */
public class ListenMessage implements Runnable {

	OnlineList context = null;
	
	public ListenMessage(OnlineList context) {
		this.context = context;
	}
	
	@Override
	public void run() {
		try {
			do {
				String response = SocketHandler.getInstance().readLine();
				if (response==null) {
					continue;
				}
				String[] args = response.split(Tag.DELIMITER);
				if (args[0].equalsIgnoreCase(Tag.SEND_TEXT)) {
					String username = args[1];
					String message = args[2];
					HashMap<String, ChatBox> lsChatBox = context.getListChatBox();
					ChatBox chatBox = lsChatBox.get(username);
					if (chatBox==null) {
						chatBox = new ChatBox(UserInformation.getInstance().getUsername(),username);
						lsChatBox.put(username, chatBox);
					}
					chatBox.addMessage(username,message);
				} 
				else if (args[0].equalsIgnoreCase(Tag.LIST_ONLINE)) {
					DefaultListModel<String> model = context.getModel();
					model.clear();
					for (int i=1;i<args.length;i++) {
						if (!(args[i].equalsIgnoreCase(UserInformation.getInstance().getUsername()))) {							
							model.addElement(args[i]);
						}
					}
					synchronized (context.getListOnline()) {
						context.getListOnline().notify();
					}
				}
				else if (args[0].equalsIgnoreCase(Tag.FILE_INFO)) {
					String username = args[1];
					String length = args[2];
					int result = JOptionPane.showConfirmDialog(null, String.format("You are receiving %s byte(s) file from %s?",length,username));
					if (result==JOptionPane.YES_OPTION) {
						SocketHandler.getInstance().send(Tag.FILE_RES, UserInformation.getInstance().getUsername(), Tag.ACCEPT, length);
						
						DataInputStream dis = new DataInputStream(SocketHandler.getInstance().getSocket().getInputStream());
						FileOutputStream fos = new FileOutputStream("download.dat");
						
						byte[] buffer = new byte[4096];
						int read = 0;
						long remain = Long.valueOf(length);
						
						while ((read=dis.read(buffer,0,(int) Math.min(remain, buffer.length)))>0){
							remain -= read;
							fos.write(buffer);
						}
						
						dis.close();
						fos.close();
					
					} else {
						SocketHandler.getInstance().send(Tag.FILE_RES, UserInformation.getInstance().getUsername(), Tag.DENY);
					}
				}
				else if (args[0].equalsIgnoreCase(Tag.FILE_RES)) {
					String username = args[1];
					String res = args[2];
					if (res.equals(Tag.ACCEPT)) {
						
						File file = context.getListChatBox().get(username).getSelectedFile();
						FileInputStream fis = new FileInputStream(file);
						DataOutputStream dos = new DataOutputStream(SocketHandler.getInstance().getSocket().getOutputStream());
						byte[] buffer = new byte[4096];
						
						while (fis.read(buffer)>0) {
							dos.write(buffer);
						}
						dos.close();
						fis.close();
					}
				}
			} while (true);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				SocketHandler.getInstance().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
