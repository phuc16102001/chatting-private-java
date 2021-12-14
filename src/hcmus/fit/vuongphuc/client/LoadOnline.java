/**
 * @package hcmus.fit.vuongphuc.client
 * @author VuongPhuc
 *
 * Dec. 14, 2021 - 1:46:25 a.m.
 * @since 2021
 * @version
 */
package hcmus.fit.vuongphuc.client;

import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import hcmus.fit.vuongphuc.constant.Tag;
import hcmus.fit.vuongphuc.ui.MyDialog;

/**
 * Description:
 *
 * @author VuongPhuc
 * @see 
 */
public class LoadOnline implements Runnable{

		OnlineList context;
		
		public LoadOnline(OnlineList context) {
			this.context = context;
		}

		@Override
		public void run() {
	
			try {
				String response = SocketHandler.getInstance().send("online", true);
				String[] args = response.split(Tag.DELIMITER);
				if (args[0].equalsIgnoreCase("online")) {
					
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
				
			} catch (IOException e) {
				MyDialog dialog = new MyDialog(context, "Error", "Cannot connect to server");
				dialog.setVisible(true);
				e.printStackTrace();
			}
			
		}
		
	
}
