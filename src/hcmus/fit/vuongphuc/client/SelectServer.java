/**
w * @package hcmus.fit.vuongphuc.client
 * @author VuongPhuc
 *
 * Dec. 10, 2021 - 2:30:58 a.m.
 * @since 2021
 * @version
 */
package hcmus.fit.vuongphuc.client;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;

/**
 * Description:
 *
 * @author VuongPhuc
 * @see 
 */
public class SelectServer extends JFrame implements ActionListener {

	JTextField txtIP = new JTextField(10);
	JTextField txtPort = new JTextField(10);
	JButton btnConnect = new JButton("Connect");
	
	private JPanel createCenter() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		panel.setBorder(new TitledBorder("Server information"));
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx=0;
		gbc.gridy=0;
		panel.add(new JLabel("Server IP:"),gbc);
		
		gbc.gridx++;
		panel.add(txtIP,gbc);
		
		gbc.gridx=0;
		gbc.gridy++;
		panel.add(new JLabel("Server port:"),gbc);
		
		gbc.gridx++;
		panel.add(txtPort,gbc);
		
		return panel;
	}
	
	private JPanel createBottom() {
		JPanel panel = new JPanel();
		
		btnConnect.addActionListener(this);
		panel.add(btnConnect);
		
		return panel;
	}
	
	public SelectServer() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setTitle("Select server");
		this.add(createCenter(),BorderLayout.CENTER);
		this.add(createBottom(),BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src==btnConnect) {
			String ip = txtIP.getText();
			String port = txtPort.getText();

			Thread t = new Thread(new CreateConnection(this, ip, port));
			t.start();
		}
	}
}
