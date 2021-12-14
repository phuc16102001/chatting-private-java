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
				if (args[0].equalsIgnoreCase("send")) {
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
