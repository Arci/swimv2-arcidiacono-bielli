package it.polimi.swim.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "message")
public class Message implements Serializable, Comparable<Message> {

	private static final long serialVersionUID = 2362561311034019242L;

	@Id
	@SequenceGenerator(name = "Sequence", sequenceName = "SEQ_CUSTOM_WD")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Sequence")
	@Column(name = "ID")
	private int id;

	@Column(name = "text", nullable = false)
	private String text;

	@Column(name = "timestamp", nullable = false)
	private long timestamp;

	@OneToOne
	@JoinColumn(name = "userID", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "helpID", nullable = false)
	private HelpRequest helpRequest;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long dateTime) {
		this.timestamp = dateTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public HelpRequest getHelpRequest() {
		return helpRequest;
	}

	public void setHelpRequest(HelpRequest helpRequest) {
		this.helpRequest = helpRequest;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Message)) {
			return false;
		}
		Message other = (Message) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Message other) {
		if (this.timestamp > other.timestamp) {
			return 1;
		} else if (this.timestamp < other.timestamp) {
			return -1;
		} else if (this.timestamp == other.timestamp) {
			return 0;
		}
		return 0;
	}

}
