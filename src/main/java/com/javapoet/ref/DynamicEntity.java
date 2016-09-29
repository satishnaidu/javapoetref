package com.javapoet.ref;

public class DynamicEntity {

	private java.lang.String firstName;

	private java.lang.String lastName;

	private java.lang.String ssnNumber;

	public java.lang.String getfirstName() {
		return firstName;
	}

	public java.lang.String getlastName() {
		return lastName;
	}

	public java.lang.String getssnNumber() {
		return ssnNumber;
	}

	@javax.persistence.Column(name = "0", length = 30)
	public void setfirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}

	@javax.persistence.Column(name = "30", length = 30)
	public void setlastName(java.lang.String lastName) {
		this.lastName = lastName;
	}

	@javax.persistence.Column(name = "60", length = 9)
	public void setssnNumber(java.lang.String ssnNumber) {
		this.ssnNumber = ssnNumber;
	}

	public com.javapoet.ref.DynamicEntity getEntityValues() {
		DynamicEntity de = new DynamicEntity();
		de.setfirstName(firstName);
		de.setlastName(lastName);
		de.setssnNumber(ssnNumber);
		return de;
	}
}
