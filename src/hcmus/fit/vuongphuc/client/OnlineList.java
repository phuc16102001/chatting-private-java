/**
 * @package hcmus.fit.vuongphuc.client
 * @author VuongPhuc
 *
 * Dec. 9, 2021 - 3:27:55 a.m.
 * @since 2021
 * @version
 */
package hcmus.fit.vuongphuc.client;

import javax.swing.*;
import javax.swing.border.*;

import hcmus.fit.vuongphuc.constant.Tag;
import hcmus.fit.vuongphuc.server.ClientSocket;
import hcmus.fit.vuongphuc.ui.MyDialog;

import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.awt.*;

/**
 * Description:
 *
 * @author VuongPhuc
 * @see 
 */
public class OnlineList extends JFrame implements ActionListener {

	private DefaultListModel<String> model = new DefaultListModel<String>();
	private JList<String> lsOnline = new JList<String>(model);
	private HashMap<String, ChatBox> lsChatBox = new HashMap<>();
	
	private Thread threadListen = null;
	
	private JButton btnLogout = new JButton("Logout");
	private JButton btnRefresh = new JButton("Refresh");

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src==btnRefresh) {
			refreshListOnline();
		} 
		else if (src==btnLogout) {
			try {
				for (String username:lsChatBox.keySet()) {
					lsChatBox.get(username).dispose();
				}
				SocketHandler.getInstance().send(Tag.LOGOUT,false);
				UserInformation.getInstance().setUsername(null);
				threadListen.interrupt();
				
				new LoginSignup();
				this.dispose();
			} catch (IOException e1) {
				MyDialog dialog = new MyDialog(null,"Error","Cannot connect to server");
				dialog.setVisible(true);
				this.dispose();
				e1.printStackTrace();
			}
		}
	}
	
	public HashMap<String, ChatBox> getListChatBox() {
		return lsChatBox;
	}
	
	public void clearList() {
		model.clear();
	}
	
	public void addOnlineUser(String username) {
		model.addElement(username);
	}
	
	private JPanel createTop() {
		JPanel panel = new JPanel();
		
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		panel.setLayout(layout);

		panel.add(new JLabel(String.format("Username: %s", UserInformation.getInstance().getUsername())));
		return panel;
	}
	
	private JScrollPane createCenter() {
		JScrollPane panel = new JScrollPane(lsOnline);
		
		panel.setBorder(new TitledBorder("Online user"));
		
		lsOnline.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JList list = (JList) e.getSource();
				if (e.getClickCount()==2) {
					int index = list.locationToIndex(e.getPoint());
					if (index!=-1) {
						String username = lsOnline.getSelectedValue();
						lsChatBox.put(username, new ChatBox(UserInformation.getInstance().getUsername(),username));
					}
				}
			}
		});
		lsOnline.setBorder(new EmptyBorder(10,10,10,10));
		
		return panel;
	}
	
	private JPanel createBottom() {
		JPanel panel = new JPanel();
		
		FlowLayout layout = new FlowLayout();
		panel.setLayout(layout);
		
		btnLogout.addActionListener(this);
		btnRefresh.addActionListener(this);
		
		panel.add(btnLogout);
		panel.add(Box.createRigidArea(new Dimension(10,0)));
		panel.add(btnRefresh);
		
		return panel;
	}
	
	private void refreshListOnline() {
		try {
			SocketHandler.getInstance().send(Tag.LIST_ONLINE, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public DefaultListModel<String> getModel() {
		return model;
	}
	
	public JList getListOnline() {
		return lsOnline;
	}
	
	public OnlineList() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setTitle("Online list");
		this.add(createTop(),BorderLayout.NORTH);
		this.add(createCenter(),BorderLayout.CENTER);
		this.add(createBottom(),BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);

		refreshListOnline();
		
		threadListen = new Thread(new ListenMessage(this));
		threadListen.start();
		
	}
	
}
