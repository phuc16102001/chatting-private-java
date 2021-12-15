/**
 * @package hcmus.fit.vuongphuc.client
 * @author VuongPhuc
 *
 * Dec. 9, 2021 - 3:26:32 a.m.
 * @since 2021
 * @version
 */
package hcmus.fit.vuongphuc.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.border.*;

import hcmus.fit.vuongphuc.constant.Tag;
import hcmus.fit.vuongphuc.ui.MyDialog;

/**
 * Description:
 *
 * @author VuongPhuc
 * @see 
 */
public class LoginSignup extends JFrame implements ActionListener {

	JButton btnLogin = new JButton("Login");
	JButton btnSignup = new JButton("Sign up");
	JTextField txtUsername = new JTextField(10);
	JTextField txtPassword = new JTextField(10);
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		String username = txtUsername.getText();
		String password = txtPassword.getText();

		if (src==btnLogin) {
			try {
				String response = SocketHandler.getInstance().send(Tag.LOGIN, new String[] {username, password}, true);
				String[] args = response.split(Tag.DELIMITER);
				
				if (args[0].equalsIgnoreCase(Tag.LOGIN) && args[1].equalsIgnoreCase(Tag.SUCCESS)) {					
					UserInformation.getInstance().setUsername(username);
					new OnlineList();
					this.dispose();
				} else {
					JDialog dialog = new MyDialog(this,"Error","Login fail");
					dialog.setVisible(true);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

				JDialog dialog = new MyDialog(this,"Error","Cannot connect to server");
				dialog.setVisible(true);
			}
		} 
		else if (src==btnSignup) {
			try {
				String response = SocketHandler.getInstance().send(Tag.SIGNUP, new String[] {username, password}, true);
				String[] args = response.split(Tag.DELIMITER);
				
				if (args[0].equalsIgnoreCase(Tag.SIGNUP) && args[1].equalsIgnoreCase(Tag.SUCCESS)) {					
					JDialog dialog = new MyDialog(this,"Error","Signup successfully");
					dialog.setVisible(true);
				} else {
					JDialog dialog = new MyDialog(this,"Error","Signup fail");
					dialog.setVisible(true);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

				JDialog dialog = new MyDialog(this,"Error","Cannot connect to server");
				dialog.setVisible(true);
			}
		}
	}
	
	private JPanel createTop() {
		JPanel panel = new JPanel();
		
		panel.add(new JLabel("Chatting application"));
		
		return panel;
	}
	
	private JPanel createCenter() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		panel.setBorder(new TitledBorder("Information"));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=0;
		
		gbc.gridy++;
		panel.add(new JLabel("Username:"),gbc);
		
		gbc.gridx++;
		panel.add(txtUsername,gbc);
		
		gbc.gridx=0;
		gbc.gridy++;
		panel.add(new JLabel("Password:"),gbc);
		
		gbc.gridx++;
		panel.add(txtPassword,gbc);
		
		return panel;
	}
	
	private JPanel createBottom() {
		JPanel panel = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.CENTER);
		panel.setLayout(layout);
		
		btnLogin.addActionListener(this);
		btnSignup.addActionListener(this);
		
		panel.add(btnLogin);
		panel.add(Box.createRigidArea(new Dimension(10,0)));
		panel.add(btnSignup);
		
		return panel;
	}
	
	public LoginSignup() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setTitle("Chat client");
		
		this.add(createTop(),BorderLayout.NORTH);
		this.add(createCenter(),BorderLayout.CENTER);
		this.add(createBottom(),BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}
}
