/**
 * @package hcmus.fit.vuongphuc.client
 * @author VuongPhuc
 *
 * Dec. 10, 2021 - 2:20:12 a.m.
 * @since 2021
 * @version
 */
package hcmus.fit.vuongphuc.client;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import javax.swing.JDialog;
import javax.swing.JFrame;

import hcmus.fit.vuongphuc.ui.MyDialog;

/**
 * Description:
 *
 * @author VuongPhuc
 * @see 
 */
public class CreateConnection implements Runnable {

	JFrame context = null;
	String ip = null;
	String port = null;
	
	public CreateConnection(JFrame context, String ip, String port) {
		this.context = context;
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public void run() {
		JDialog dialog = new MyDialog(context,"Client connecting","Connecting to server...");
		dialog.setVisible(true);
		
		try {
			SocketAddress sockAddress = new InetSocketAddress(ip,Integer.valueOf(port));
			Socket socket = new Socket();
			socket.connect(sockAddress,5000);
			
			SocketHandler.getInstance().setSocket(socket);
			
			new LoginSignup();
			dialog.dispose();
			context.dispose();
			
		} catch (Exception e) {
			e.printStackTrace();
			dialog.dispose();
			
			JDialog failDialog = new MyDialog(context,"Client connecting","Cannot create connection");
			failDialog.setVisible(true);
		}
	}
}