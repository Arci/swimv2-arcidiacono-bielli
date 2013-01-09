package it.polimi.swim.model;

import it.polimi.swim.enums.UserType;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 7705714975162129986L;

	@Id
	@SequenceGenerator(name = "Sequence", sequenceName = "SEQ_CUSTOM_WD")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Sequence")
	@Column(name = "ID")
	private int id;

	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserType type;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "surname")
	private String surname;

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "city")
	private String city;

	@Column(name = "phone")
	private int phone;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_abilities", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "abilityID"))
	private Set<Ability> abilities;

	/**
	 * List of ability request created
	 */
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private Set<AbilityRequest> abilityRequest;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public Set<Ability> getAbilities() {
		return abilities;
	}

	public void setAbilities(Set<Ability> abilities) {
		this.abilities = abilities;
	}

	public Set<AbilityRequest> getAbilityRequest() {
		return abilityRequest;
	}

	public void setAbilityRequest(Set<AbilityRequest> abilityRequest) {
		this.abilityRequest = abilityRequest;
	}

	public void addAbility(Ability ability) {
		abilities.add(ability);
	}

	public void removeAbility(Ability ability) {
		abilities.remove(ability);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

}
