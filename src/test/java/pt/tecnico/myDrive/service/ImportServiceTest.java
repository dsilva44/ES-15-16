package pt.tecnico.myDrive.service;


import pt.tecnico.myDrive.domain.Manager;

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
	+"  <perm>rwxdr-x-</perm>"
	+"</dir>"
	+"<link id=\"5\">"
	+"  <path>/home/jtb</path>"
	+"  <name>doc</name>"
	+"  <owner>jtb</owner>"
	+"  <perm>r-xdr-x-</perm>"
	+"  <value>/home/jtb/documents</value>"
	+"</link>"
	+"<dir id=\"7\">"
	+"  <path>/home/jtb</path>"
	+"  <owner>jtb</owner>"
	+"  <name>bin</name>"
	+"  <perm>rwxd--x-</perm>"
	+"</dir>"
	+"<app id=\"9\">"
	+"  <path>/home/jtb/bin</path>"
	+"  <name>hello</name>"
	+"  <owner>jtb</owner>"
	+"  <perm>rwxd--x-</perm>"
	+"  <method>pt.tecnico.myDrive.app.Hello</method>"
	+"</app>"
	+"</myDrive>";


	@Override
	protected void populate() {
		Manager manager = Manager.getInstance();
	}





}
