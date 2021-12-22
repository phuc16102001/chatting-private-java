/**
 * @package hcmus.fit.vuongphuc.client
 * @author VuongPhuc
 *
 * Dec. 9, 2021 - 3:28:07 a.m.
 * @since 2021
 * @version
 */
package hcmus.fit.vuongphuc.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import hcmus.fit.vuongphuc.constant.Tag;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * Description:
 *
 * @author VuongPhuc
 * @see 
 */
public class ChatBox extends JFrame implements ActionListener, WindowListener {

	private String username;
	private String targetName;
	
	private JTextArea txtChatBox = new JTextArea(10,10);
	private JTextField txtInput = new JTextField(20);
	private JButton btnSend = new JButton("Send");
	private JButton btnFile = new JButton(UIManager.getIcon("FileView.fileIcon"));
	
	private File selectedFile = null;
	private OnlineList context = null;
	
	public void addMessage(String username, String message) {
		txtChatBox.append(String.format("%s: %s\n",	username,message));
	}
	
	public File getSelectedFile() {
		return selectedFile;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src==btnSend) {
			String message = txtInput.getText();
			txtInput.setText(null);
			try {
				SocketHandler.getInstance().send(Tag.SEND_TEXT, targetName, message);
				addMessage(UserInformation.getInstance().getUsername(), message);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} 
		else if (src==btnFile) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Select file");
			int returnValue = fileChooser.showOpenDialog(this);
			if (returnValue==JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				try {
					SocketHandler.getInstance().send(Tag.FILE_INFO, UserInformation.getInstance().getUsername(), targetName, String.valueOf(selectedFile.length()), selectedFile.getName());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	private JScrollPane createCenter() {
		JScrollPane panel = new JScrollPane(txtChatBox);
	
		panel.setBorder(new TitledBorder("Message"));
		txtChatBox.setEditable(false);
		txtChatBox.setBorder(new EmptyBorder(5,5,5,5));
		
		return panel;
	}
	
	private JPanel createBottom() {
		JPanel panel = new JPanel();
		
		btnSend.addActionListener(this);
		btnFile.addActionListener(this);
		
		panel.add(txtInput);
		panel.add(btnSend);
		panel.add(btnFile);
		
		return panel;
	}
	
	public ChatBox(OnlineList context, String username, String targetName) {
		this.context = context;
		this.username = username;
		this.targetName = targetName;
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle(String.format("Chat box (%s-%s)", this.username, this.targetName));
		this.addWindowListener(this);
		
		this.add(createCenter(),BorderLayout.CENTER);
		this.add(createBottom(),BorderLayout.SOUTH);
		
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		context.removeChatBox(targetName);
		this.dispose();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
