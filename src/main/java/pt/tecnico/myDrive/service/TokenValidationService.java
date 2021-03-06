package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.exception.InvalidTokenException;
import pt.tecnico.myDrive.exception.MyDriveException;

public class TokenValidationService extends MyDriveService {
	protected Login session;
	private Long token;

	public TokenValidationService(long token) {
        this.token = token;
    }

	@Override
	protected void dispatch() throws MyDriveException {
		Manager m = getManager();
		session = m.getLoginByToken(token);
		if (session == null){
			throw new InvalidTokenException();
		}
	}

}
