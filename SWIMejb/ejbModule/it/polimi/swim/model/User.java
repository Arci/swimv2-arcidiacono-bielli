package it.polimi.swim.model;

import it.polimi.swim.enums.UserType;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User implements Serializable {

	private static final long serialVersionUID = 7705714975162129986L;

	@Id
	@SequenceGenerator(name = "Sequence", sequenceName = "SEQ_CUSTOM_WD")  
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "Sequence")  
	@Column(name="ID")
	private int id;

	@Column(name="type", nullable=false)
	@Enumerated(EnumType.STRING) 
	private UserType type;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="surname")
	private String surname;

	@Column(name="username", nullable=false, unique=true)
	private String username;
	
	@Column(name="email", nullable=false, unique=true)
	private String email;
	
	@Column(name="password", nullable=false)
	private String password;
	
	@Column(name="city")
	private String city;
	
	@Column(name="phone")
	private int phone;
	
	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}
	
}
