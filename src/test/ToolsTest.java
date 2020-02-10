package test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import tools.Handler;
import tools.SessionPool;

public class ToolsTest {
	
	// ----- Attributes -----
	
	
	private Handler handler = Handler.getInstance();
	private SessionPool sessionPool = SessionPool.getInstance();
	
	
	// ----- Config methods -----
	
	
	@BeforeClass
	public static void setup() {
		System.out.println("test !");
	}
	
	
	// ----- Test methods -----

	
	/**
	 * Test the handler
	 */
	@Test
	public void testHandler() {
		fail("Not implemented yet !");
	}
	
	/**
	 * Test the session pool
	 */
	@Test
	public void testSessionPool() {
		assertEquals(32, this.sessionPool.generateSessionId().length());
	}

}
