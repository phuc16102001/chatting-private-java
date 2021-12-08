/**
 * @package hcmus.fit.vuongphuc.server
 * @author VuongPhuc
 *
 * Dec. 9, 2021 - 3:58:36 a.m.
 * @since 2021
 * @version
 */
package hcmus.fit.vuongphuc.server;

import java.io.*;

/**
 * Description:
 *
 * @author VuongPhuc
 * @see 
 */
public class FileHandler {

	private static final String ACCOUNT_PATH = "account.dat";
	private static final String DELIMITER = "-";
	
	public static Boolean login(String username, String password) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(ACCOUNT_PATH));
		} catch (FileNotFoundException e) {
			return false;
		}
		
		String line = null;
		while ((line=reader.readLine())!=null) {
			line = line.replace("\n", "");
			String[] args = line.split(DELIMITER);
			if (args[0].equals(username) && args[1].equals(password)) {
				reader.close();
				return true;
			}
		}
		
		reader.close();
		return false;
	}
	
	public static Boolean signup(String username, String password) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT_PATH));
		
			String line = null;
			while ((line=reader.readLine())!=null) {
				String[] args = line.split(DELIMITER);
				if (username.equalsIgnoreCase(args[0])) {
					reader.close();
					return false;
				}
			}	
			reader.close();
		} catch (FileNotFoundException e) {
			File file = new File(ACCOUNT_PATH);
			file.createNewFile();
		}

		BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_PATH,true));
		writer.write(username+DELIMITER+password+"\n");
		writer.close();
		return true;
	}
	
}
