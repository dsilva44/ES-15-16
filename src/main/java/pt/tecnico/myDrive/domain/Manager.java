package pt.tecnico.myDrive.domain;

import org.jdom2.Document;
import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.UserAlreadyExistsException;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;



public class Manager extends Manager_Base {
	
	
	// manager use Singleton design pattern
    public static Manager getInstance() {
    	Manager manager = FenixFramework.getDomainRoot().getManager();
    	if (manager != null) {
    		return manager;
    	}
    	return new Manager();
    } 
    
    private Manager() {
		this.setRoot(FenixFramework.getDomainRoot());
		this.setIdCounter(0);

		User superUser = new User("root", "***", "Super User", "rwxdr-x-", this, null);

		File startHome = new Directory("/", "rwxdr-x-", this, superUser, null);
		startHome.setParent((Directory) startHome);

		Directory home = startHome.createDirectory("home", this, superUser);
		Directory rootHome = home.createDirectory("root", this, superUser);
		superUser.setHome(rootHome);
	}
    
	
        
    public User getUserByUsername(String username) {
    	for (User user: this.getUserSet()) {
    		if (user.getUsername().equals(username))
    			return user;
    	}
    	return null;
    }
    
    public boolean hasUser(String username) {
    	return this.getUserByUsername(username) != null;
    }
    
   
    @Override
    public void addUser(User newUser) {
    	super.addUser(newUser);
    }
    
    
    public void createNewUser(String username){   
    	this.createNewUser(username, username, username, "rwxd----");
    }
    
    
    // miss exceptions
    public void createNewUser(String username, String password, String name, String umask){
    	User newUser = new User(username, password, name, umask, this, null);
    	Directory userHome = this.getHomeDirectory().createDirectory(username, this, newUser);
    	newUser.setHome(userHome);
    	this.addUser(newUser);
    }
    
    
    /* C
    public void createNewUser(String username) throws UserAlreadyExistsException, EmptyUsernameException, InvalidUsernameException{      
    	
        this.validateUsername(username);

    	User newUser = new User(username);
    	this.addUser(newUser);
    	newUser.setManager(this);
    }
    
    public boolean validateUsername(String username) throws UserAlreadyExistsException,EmptyUsernameException, InvalidUsernameException{
        
        if (this.hasUser(username)) {
            throw new UserAlreadyExistsException(username);
        }
        
        boolean isAlphanumeric = Pattern.matches("^[a-zA-Z0-9]*$", username);    

        if (username.isEmpty()) throw new EmptyUsernameException();
        else if (!isAlphanumeric) throw new InvalidUsernameException();
    }
	C */ 
    
    
    public File getFileById(int id) {
    	for (File file: this.getFileSet()) {
    		if (file.getId().equals(id))
    			return file;
    	}
    	return null;
    }
    
    public boolean hasFile(int id) {
    	return this.getFileById(id) != null;
    }
 
    

    @Override 
    public void addFile(File newFile) throws FileAlreadyExistsException {
    	if (this.hasFile(newFile.getId())) {
    		throw new FileAlreadyExistsException(newFile.getId());
    	}
    	super.addFile(newFile);
    }
    
    public int getNextIdCounter() {
    	int currCounter = this.getIdCounter();
    	this.setIdCounter(currCounter+1);
    	return currCounter;
    }
    
    
    public Directory getHomeDirectory() {
    	for (File f: this.getFileSet()) {
    		if (f.getName().equals("/")) {
    			Directory pathStart = (Directory) f;
    			for (File h: pathStart.getFileSet()) {
    				if (h.getName().equals("home")) {
    					return (Directory) h;
    				}
    			}

    		}
    	}
		return null;
    }
    


    public Directory createMissingDirectories(String dirs){
		String[] tokens = dirs.split("/");
		String building="";
		User sudo = getUserByUsername("root");
		Directory barra = getHomeDirectory().getParent();

		for(int i=1;i<tokens.length;i++){
			if (barra.lookup(building+'/'+tokens[i])!=null){ //dir exists
				building+='/'+tokens[i];
			}
			else{
				barra.lookup(building).createDirectory(tokens[i],this,sudo);
				building+='/'+tokens[i]; //to add new dir to building
			}
		}
		return (Directory) barra.lookup(dirs);
	}
/*
    public Directory lookUpDir(String pathname){};
*/

    public void xmlImport(Element rootDrive) throws UnsupportedEncodingException {
		/*
		for (Element userNode: rootDrive.getChildren("user")){
			new User(this,userNode);
		}
		*/
		for (Element dirNode: rootDrive.getChildren("dir")){
			new Directory(this,dirNode);
		}
		for (Element plainNode : rootDrive.getChildren("plain")) {
			new PlainFile(this	, plainNode);
		}

		for (Element linkNode: rootDrive.getChildren("link")){
			new Link(this,linkNode);
		}
		for (Element appNode: rootDrive.getChildren("app")){
			new App(this,appNode);
		}

    }

    public Document xmlExport() {
        Element element = new Element("myDrive");
        Document doc = new Document(element);

        for (User u: getUserSet()) {
        	if (!u.getUsername().equals("root"))
        		element.addContent(u.xmlExport());
        }
            
        /*for (File f: getFileSet())
            element.addContent(f.xmlExport());*/

        return doc;
        
        
        
    }

}
