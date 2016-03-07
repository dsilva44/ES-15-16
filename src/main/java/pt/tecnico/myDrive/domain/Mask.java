package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;

public enum Mask{
	READ('r'),
	WRITE('w'),
	EXEC('x'),
	DELETE('d');

	private char value;

	private Mask(char value){
		this.value = value;
	}

	public char getValue(){
		return this.value;
	}
}