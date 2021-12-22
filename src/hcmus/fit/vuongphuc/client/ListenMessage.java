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
				String[] args = response.split(Tag.DELIMITER);
				String route = args[0];
				if (route.equalsIgnoreCase(Tag.SEND_TEXT)) {
					String username = args[1];
					String message = args[2];
					HashMap<String, ChatBox> lsChatBox = context.getListChatBox();
					ChatBox chatBox = lsChatBox.get(username);
					if (chatBox==null) {
						chatBox = new ChatBox(context,UserInformation.getInstance().getUsername(),username);
						lsChatBox.put(username, chatBox);
					}
					chatBox.addMessage(username,message);
				} 
				else if (route.equalsIgnoreCase(Tag.LIST_ONLINE)) {
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
				else if (route.equalsIgnoreCase(Tag.FILE_INFO)) {
					String from = args[1];
					String to = args[2];
					String length = args[3];
					String fileName = args[4];
					int result = JOptionPane.showConfirmDialog(null, String.format("Do you want to receive '%s' from '%s'?",fileName,from));
					if (result==JOptionPane.YES_OPTION) {
						SocketHandler.getInstance().send(Tag.FILE_RES, to, from, Tag.ACCEPT, length, fileName);
					} else {
						SocketHandler.getInstance().send(Tag.FILE_RES, to, from, Tag.DENY, length, fileName);
					}
				}
				else if (route.equalsIgnoreCase(Tag.FILE_RES)) {
					String from = args[1];
					String to = args[2];
					String res = args[3];
					String length = args[4];
					String fileName = args[5];
					if (res.equalsIgnoreCase(Tag.ACCEPT)) {
						SocketHandler.getInstance().send(Tag.SEND_FILE, to, from, length, fileName);
						
						File file = context.getListChatBox().get(from).getSelectedFile();
						FileInputStream fis = new FileInputStream(file);
						
						DataOutputStream dos = SocketHandler.getInstance().getWriter();
						byte[] buffer = new byte[4096];
						
						while (fis.read(buffer)>0) {
							dos.write(buffer);
						}
						
						fis.close();
					} else {
						MyDialog dialog = new MyDialog(null,"File transfer",String.format("User %s denied to receive file", from));
						dialog.setVisible(true);
					}
					System.out.println("Done sending file");
				}
				else if (route.equalsIgnoreCase(Tag.SEND_FILE)) {
					Long length = Long.parseLong(args[3]);
					String fileName = args[4];
					
					FileOutputStream fos = new FileOutputStream(fileName);
					DataInputStream dis = SocketHandler.getInstance().getReader();
					
					byte[] buffer = new byte[4096];
					int read = 0;
					long remain = length;
					
					System.out.println("Begin to write to file with size: "+length);
					while ((read=dis.read(buffer,0, (int)Math.min(buffer.length,remain)))>0){
						remain -= read;
						fos.write(buffer);
						System.out.println("Byte remain: "+remain);
					}
					fos.close();
					dis.skip(dis.available());
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
