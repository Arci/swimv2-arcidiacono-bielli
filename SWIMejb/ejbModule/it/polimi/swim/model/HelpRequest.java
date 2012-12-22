package it.polimi.swim.model;

import it.polimi.swim.enums.HelpState;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "help_request")
public class HelpRequest implements Serializable {

	private static final long serialVersionUID = 5146109596245637271L;

	@Id
	@SequenceGenerator(name = "Sequence", sequenceName = "SEQ_CUSTOM_WD")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Sequence")
	@Column(name = "ID")
	private int id;

	@ManyToOne
	@JoinColumn(name = "userID", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "helperID", nullable = false)
	private User helper;

	@OneToOne
	@JoinColumn(name = "abilityID", nullable = false)
	private Ability ability;

	@Column(name = "opening_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date openingDate;

	@Column(name = "closing_date")
	@Temporal(TemporalType.DATE)
	private Date closingDate;

	@Column(name = "vote")
	private int vote;

	@Column(name = "state", nullable = false)
	@Enumerated(EnumType.STRING)
	private HelpState state;

	/**
	 * List of messages associated in this help request
	 */
	@OneToMany(mappedBy = "helpRequest")
	private List<Message> messages;

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

	public User getHelper() {
		return helper;
	}

	public void setHelper(User helper) {
		this.helper = helper;
	}

	public Ability getAbility() {
		return ability;
	}

	public void setAbility(Ability ability) {
		this.ability = ability;
	}

	public Date getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

	public Date getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}

	public HelpState getState() {
		return state;
	}

	public void setState(HelpState state) {
		this.state = state;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

}
