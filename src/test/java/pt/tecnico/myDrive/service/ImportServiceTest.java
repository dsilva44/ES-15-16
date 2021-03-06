package pt.tecnico.myDrive.service;

import static org.junit.Assert.*;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;
import pt.tecnico.myDrive.domain.Manager;
import org.jdom2.Document;

import java.io.IOException;
import java.io.StringReader;

public class ImportServiceTest extends AbstractServiceTest{

	private final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+"<myDrive>"
			+"<user username=\"jtb\">"
			+"  <password>fermento</password>"
			+"  <name>Joaquim Teófilo Braga</name>"
			+"  <home>/home/jtb</home>"
			+"  <mask>rwxdr-x-</mask>"
			+"</user>"
			+"<plain id=\"3\">"
			+"  <path>/home/jtb</path>"
			+"  <name>profile</name>"
			+"  <owner>jtb</owner>"
			+"  <perm>rw-dr---</perm>"
			+"  <contents>Primeiro chefe de Estado do regime republicano (acumulando com a chefia do governo), numa capacidade provisória até à eleição do primeiro presidente da República.</contents>"
			+"</plain>"
			+"<dir id=\"4\">"
			+"  <path>/home/jtb</path>"
			+"  <name>documents</name>"
			+"  <owner>jtb</owner>"
			+"  <perm>rwxdrwxd</perm>"
			+"</dir>"
			+"<link id=\"5\">"
			+"  <path>/home/jtb</path>"
			+"  <name>doc</name>"
			+"  <owner>jtb</owner>"
			+"  <perm>r-xdr-x-</perm>"
			+"  <value>/home/jtb/documents</value>"
			+"</link>"
			+"<plain id=\"8\">"
			+"  <path>/home/jtb/documents</path>"
			+"  <name>file</name>"
			+"  <owner>jtb</owner>"
			+"  <perm>rwxdr-x-</perm>"
			+"  <contents>Pri</contents>"
			+"</plain>"
			+"<dir id=\"7\">"
			+"  <path>/home/jtb</path>"
			+"  <owner>jtb</owner>"
			+"  <name>bin</name>"
			+"  <perm>rwxd--x-</perm>"
			+"</dir>"
			+"</myDrive>";


	@Override
	protected void populate() {
		Manager manager = Manager.getInstance();
	}

	@Test
	public void success() throws JDOMException, IOException {
		Document doc = new SAXBuilder().build(new StringReader(xml));
		ImportService service = new ImportService(doc);
		service.execute();

		Manager manager = Manager.getInstance();

		assertTrue("1", manager.hasUser("jtb"));
		assertEquals("2", manager.fetchUser("jtb", "fermento").getHome().getFileByName("profile").read(manager.fetchUser("jtb", "fermento")), "Primeiro chefe de Estado do regime republicano (acumulando com a chefia do governo), numa capacidade provisória até à eleição do primeiro presidente da República.");
		assertEquals("3", "rwxdrwxd", manager.fetchUser("jtb", "fermento").getHome().getFileByName("documents").getPermissions());
		assertEquals("4", "file", manager.fetchUser("jtb", "fermento").getHome().lookup("/home/jtb/doc/file", manager.fetchUser("jtb", "fermento")).getName());
		assertEquals("5", "rwxd--x-", manager.fetchUser("jtb", "fermento").getHome().getFileByName("bin").getPermissions());
	}



}