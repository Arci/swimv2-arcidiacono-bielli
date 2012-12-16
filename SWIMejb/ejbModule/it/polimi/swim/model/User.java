package it.polimi.swim.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User implements Serializable {

	private static final long serialVersionUID = 7705714975162129986L;

	@Id
	private int identificationNumber;
	
	@Column(name="name", nullable=false, unique=true)
	private String name;

	public int getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(int i) {
		this.identificationNumber = i;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
