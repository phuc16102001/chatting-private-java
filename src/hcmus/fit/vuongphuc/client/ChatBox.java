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

import java.awt.*;
import java.awt.event.*;

/**
 * Description:
 *
 * @author VuongPhuc
 * @see 
 */
public class ChatBox extends JFrame implements ActionListener {

	private String username;
	
	private JTextArea txtChatBox = new JTextArea(10,10);
	private JTextField txtInput = new JTextField(20);
	private JButton btnSend = new JButton("Send");
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src==btnSend) {
			String message = txtInput.getText();
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
		
		panel.add(txtInput);
		panel.add(btnSend);
		
		return panel;
	}
	
	public ChatBox(String username) {
		this.username = username;
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setTitle(String.format("Chat box (%s)", this.username));
		
		this.add(createCenter(),BorderLayout.CENTER);
		this.add(createBottom(),BorderLayout.SOUTH);
		
		this.pack();
		this.setVisible(true);
	}
}
