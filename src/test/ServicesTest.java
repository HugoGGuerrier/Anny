package test;

import static org.junit.Assert.*;
import org.junit.Test;
import services.message.CreateMessage;
import services.message.DeleteMessage;
import services.message.ModifyMessage;
import services.message.SearchMessage;
import services.user.CreateUser;
import services.user.DeleteUser;
import services.user.LoginUser;
import services.user.LogoutUser;
import services.user.ModifyUser;
import services.user.SearchUser;

public class ServicesTest {
	
	// ----- Attributes -----
	
	
	// --- Follower services
	
	// --- Message services
	private CreateMessage createMessage = CreateMessage.getInstance();
	private DeleteMessage deleteMessage = DeleteMessage.getInstance();
	private ModifyMessage modifyMessage = ModifyMessage.getInstance();
	private SearchMessage searchMessage = SearchMessage.getInstance();
	
	// --- User services
	private CreateUser createUser = CreateUser.getInstance();
	private DeleteUser deleteUser = DeleteUser.getInstance();
	private LoginUser loginUser = LoginUser.getInstance();
	private LogoutUser logoutUser = LogoutUser.getInstance();
	private ModifyUser modifyUser = ModifyUser.getInstance();
	private SearchUser searchUser = SearchUser.getInstance();
	
	
	// ----- Test methods -----
	
	
	@Test
	public void testFollower() {
		fail("Not implemented yet !");
	}
	
	@Test
	public void testMessage() {
		fail("Not implemented yet !");
	}
	
	@Test
	public void testUser() {
		fail("Not implemented yet !");
	}

}
