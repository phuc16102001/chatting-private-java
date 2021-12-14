/**
 * @package hcmus.fit.vuongphuc.client
 * @author VuongPhuc
 *
 * Dec. 14, 2021 - 5:07:58 p.m.
 * @since 2021
 * @version
 */
package hcmus.fit.vuongphuc.client;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.DefaultListModel;

import hcmus.fit.vuongphuc.constant.Tag;

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
