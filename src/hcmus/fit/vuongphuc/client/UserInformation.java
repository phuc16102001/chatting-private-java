/**
 * @package hcmus.fit.vuongphuc.client
 * @author VuongPhuc
 *
 * Dec. 14, 2021 - 8:41:42 a.m.
 * @since 2021
 * @version
 */
package hcmus.fit.vuongphuc.client;

/**
 * Description:
 *
 * @author VuongPhuc
 * @see 
 */
public class UserInformation {

	private static UserInformation object = null;
	private String username = null;
	
	private UserInformation() {}
	public static UserInformation getInstance() {
		if (object==null) {
			object = new UserInformation();
		}
		return object;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
}
