package serial;

import java.io.Serializable;
/*
 * @see wiki Java SE
 */
public class Personne implements Serializable {

	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	
	public Personne(String firstName, String lastName) {
		this.setFirstName(firstName);
		this.setLastName(lastName);

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
