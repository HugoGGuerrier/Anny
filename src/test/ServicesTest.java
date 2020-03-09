package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import services.follow.CreateFollow;
import services.follow.DeleteFollow;
import services.follow.SearchFollow;
import services.message.CreateMessage;
import services.message.DeleteMessage;
import services.message.ModifyMessage;
import services.message.SearchMessage;
import services.user.CreateUser;
import services.user.DeleteUser;
import services.user.ModifyUser;
import services.user.SearchUser;

public class ServicesTest {
	
	// ----- Attributes -----
	
	
	// --- Follow services
	private CreateFollow createFollow = CreateFollow.getInstance();
	private DeleteFollow deleteFollow = DeleteFollow.getInstance();
	private SearchFollow searchFollow = SearchFollow.getInstance();
	
	// --- Message services
	private CreateMessage createMessage = CreateMessage.getInstance();
	private DeleteMessage deleteMessage = DeleteMessage.getInstance();
	private ModifyMessage modifyMessage = ModifyMessage.getInstance();
	private SearchMessage searchMessage = SearchMessage.getInstance();
	
	// --- User services
	private CreateUser createUser = CreateUser.getInstance();
	private DeleteUser deleteUser = DeleteUser.getInstance();
	private ModifyUser modifyUser = ModifyUser.getInstance();
	private SearchUser searchUser = SearchUser.getInstance();
	
	
	// ----- Config methods -----
	
	
	@BeforeClass
	public static void setup() {
		// TODO : Faire des insertions de test dans la base de données
	}
	
	@AfterClass
	public static void tearDown() {
		// TODO : Supprimer toutes les entrées de test dans la base de données
	}
	
	
	// ----- Test methods -----
	
	
	@Test
	public void testFollow() {
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
	
	@Test
	public void testLogin() {
		fail("Not implemented yet !");
	}

}
