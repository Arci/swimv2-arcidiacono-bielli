package it.polimi.swim.model;

import it.polimi.swim.enums.RequestState;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ability_request")
public class AbilityRequest implements Serializable {

	private static final long serialVersionUID = -4285304018187196826L;

	@Id
	@SequenceGenerator(name = "Sequence", sequenceName = "SEQ_CUSTOM_WD")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Sequence")
	@Column(name = "ID")
	private int id;

	@ManyToOne
	private User user;

	@Column(name = "text", nullable = false)
	private String openingDate;

	@Column(name = "state", nullable = false)
	@Enumerated(EnumType.STRING)
	private RequestState state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}

	public RequestState getState() {
		return state;
	}

	public void setState(RequestState state) {
		this.state = state;
	}

}
