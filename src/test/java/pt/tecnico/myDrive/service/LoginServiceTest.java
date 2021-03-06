package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.exception.*;

public class LoginServiceTest extends AbstractServiceTest {


	@Override
	protected void populate() {
		Manager manager = Manager.getInstance();
		new User(manager, "Existent15Chars");
	}

	//1
	@Test(expected = InvalidUsernameOrPasswordException.class)
	public void userDoesNotExist() {
		LoginService service = new LoginService("DoesNotExist", "DoesNotExist");
		service.execute();
	}

	//2
	@Test(expected = InvalidUsernameOrPasswordException.class)
	public void wrongPassword() {
		LoginService service = new LoginService("Existent15Chars", "ExistentWrong");
		service.execute();
	}

	//3
	@Test
	public void successUserLogin() {
		LoginService service = new LoginService("Existent15Chars", "Existent15Chars");
		service.execute();

		Manager manager = Manager.getInstance();
		long token = service.result();
		User user = manager.getLoginByToken(token).getCurrentUser();

		assertThat("LoginToken is not a long", token, instanceOf(long.class));
		assertEquals("User from Token does not match", user.getName(), "Existent15Chars");
	}

	//4
	@Test(expected = InvalidUsernameOrPasswordException.class)
	public void invalidUsername() {
		LoginService service = new LoginService("Invalid+", "Invalid+");
		service.execute();
	}

	//5
	@Test(expected = InvalidUsernameOrPasswordException.class)
	public void wrongRootPassword() {
		LoginService service = new LoginService("root", "New");
		service.execute();
	}

	//6
	@Test
	public void successRootLogin() {
		LoginService service = new LoginService("root", "***");
		service.execute();

		Manager manager = Manager.getInstance();
		long token = service.result();
		User user = manager.getLoginByToken(token).getCurrentUser();

		assertThat("LoginToken is not a long", token, instanceOf(long.class));
		assertEquals("User from Token does not match", user.getName(), "Super User");
	}

	//7
	@Test(expected = EmptyUsernameException.class)
	public void emptyUsername() {
		LoginService service = new LoginService("", "New");
		service.execute();
	}

	//8
	@Test(expected = PasswordTooSmallException.class)
	public void PasswordTooSmall() {
		Manager manager = Manager.getInstance();
		new User(manager, "Exists");

		LoginService service = new LoginService("Exists", "Exists");
		service.execute();
	}

	//9
	@Test
	public void PasswordHas8Char() {
		Manager manager = Manager.getInstance();
		new User(manager, "Existent");

		LoginService service = new LoginService("Existent", "Existent");
		service.execute();

		long token = service.result();
		User user = manager.getLoginByToken(token).getCurrentUser();

		assertThat("LoginToken is not a long", token, instanceOf(long.class));
		assertEquals("User from Token does not match", user.getName(), "Existent");
	}

}
