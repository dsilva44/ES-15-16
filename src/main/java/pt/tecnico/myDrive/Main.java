package pt.tecnico.myDrive;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.App;


public class Main {
	static final Logger log = LogManager.getRootLogger();
	
	public static void main(String[] args){
		
		try {
			if (args.length == 0) {
				setup();
			}
			else {
				for (String s: args) {
	    			xmlScan(new java.io.File(s));
	    		}
				print();
			}
			
		} finally { FenixFramework.shutdown(); }
	}

	
    @Atomic
    public static void setup() {	
    	if (FenixFramework.getDomainRoot().getManager() != null) {
    		return;
    	}

		User root = Manager.getInstance().fetchUser("root", "***");
		Directory home = (Directory) Manager.getInstance().getRootDirectory().lookup("/", root);
		log.trace(home.getName());
    	/*
    	log.trace("Manager: " + Manager.getInstance());
    	User root = Manager.getInstance().fetchUser("root", "***");
    	lsDir(root);
    	User user1 = new User(Manager.getInstance(), "DAVID");
    	Directory home = (Directory) Manager.getInstance().getRootDirectory().getFileByName("home");
    	PlainFile file1 = new PlainFile("README", user1, home, "batata"); 	
    	App app1 = new App("APPME", user1, home, "batata");    	
    	Directory dir1 = new Directory("bin", user1, home);
    	PlainFile file2 = new PlainFile("binfile", user1, dir1, "batata");
    	PlainFile file3 = new PlainFile("binfile2", user1, dir1, "batata");
    	App app2 = new App("binapp", user1, dir1, "batata");  
    	
    	System.out.println("========================================================");
    	lsDir(root);
    	
    	log.trace(Manager.getInstance().getRootDirectory().getName());
    	log.trace(home.getName());
    	log.trace(home.getFileSet().size());
    	log.trace(Manager.getInstance().getRootDirectory().lookup("home/root", root).getName());
    	
    	Login login = new Login("DAVID", "DAVID");
    	log.trace("-------Login------");
    	log.trace("currUser = " + login.getCurrentUser().getName());
    	log.trace("currDir = " + login.getCurrentDir().getName());
    	log.trace("token = " + login.getToken() );
    	log.trace("password of David = " + user1.validatePassword("DAVID"));
    	
    	new User(Manager.getInstance(), "ddd");
    	Login login2 = new Login("ddd", "ddd");
    	log.trace("token2 = " + login2.getToken());
    	Manager.getInstance().removeInactiveLogins();
    	
    	Login loginRoot = new Login("root", "***");
    	log.trace(loginRoot.getCurrentUser().getName());
    	
    	
    	
    	Long invToken = login.getToken();
    	login.setLastActivity(login.getLastActivity().minusHours(3));
    	for (int i = 0; i<10; i++) {
    		Login l = Manager.getInstance().getLoginByToken(invToken);
    	}
    	
    	

    	dir1.remove();*/
    	//file1.remove();
    	//app1.remove();
    	//Manager.getInstance().getRootDirectory().lookup("/home/DAVID").remove();

    	System.out.println("========================================================");
    	lsDir(root); 

        /*DAVID
	   	User user1 = new User(Manager.getInstance(), "DAVID");
    	PlainFile file1 = new PlainFile("README", user1, Manager.getInstance().getHomeDirectory(), "batata");
    	PlainFile file2 = new PlainFile("BLA", user1, Manager.getInstance().getHomeDirectory(), "batata");
	   	Manager.getInstance().getHomeDirectory().showContent();
	   	Manager.getInstance().getRootDirectory().lookup("/home/DAVID").showContent();
	   	file1.setName("\0");
	   	Manager.getInstance().getHomeDirectory().showContent();
	   	System.out.println(file1.getLastModified());
	   	DAVID*/



		//xmlPrint();
	}

	public static void lsDir(User user) {
		Directory home = (Directory) Manager.getInstance().getRootDirectory().getFileByName("home");
		List<File> files = home.getOrderByNameFileList(user);
		System.out.println(".");
		System.out.println("..");
		for (File f : files) {
			System.out.println(f.getName());
		}
	}

	@Atomic
	public static void print() {
		log.trace("Print: " + FenixFramework.getDomainRoot());
		Manager manager = Manager.getInstance();

		for (User u: manager.getUserSet()) {
			System.out.println("User:" + u.getName()+ " has " + u.getFileSet().size() + " files:");
			for (File f: u.getFileSet())
				System.out.println("\t" + f.getName() + " contains.... ");
		}
	}


	@Atomic
    public static void xmlScan(java.io.File file) {
        log.trace("xmlScan: " + FenixFramework.getDomainRoot());  
		Manager manager = Manager.getInstance();
		SAXBuilder builder = new SAXBuilder();
		try {
		    Document document = (Document)builder.build(file);
		    manager.xmlImport(document.getRootElement());
		} catch (JDOMException | IOException e) {
		    e.printStackTrace();
		}
	}

    @Atomic
    public static void xmlPrint() {
        log.trace("[Main:xmlPrint] " + FenixFramework.getDomainRoot());
		Document doc = Manager.getInstance().xmlExport();
		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		try { xmlOutput.output(doc, new PrintStream(System.out));
		} catch (IOException e) { System.out.println(e.getMessage()); }
    }
    
}