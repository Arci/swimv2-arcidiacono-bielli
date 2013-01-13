package it.polimi.swim.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ability")
public class Ability implements Serializable {

	private static final long serialVersionUID = -4392820355152576493L;

	@Id
	@SequenceGenerator(name = "Sequence", sequenceName = "SEQ_CUSTOM_WD")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Sequence")
	@Column(name = "ID")
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Ability)) {
			return false;
		}
		Ability other = (Ability) obj;
		if (name != other.name) {
			return false;
		}
		return true;
	}

}