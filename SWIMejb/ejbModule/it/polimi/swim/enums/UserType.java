package it.polimi.swim.enums;

/**
 * This class define the type of user
 * 
 * @author Arcidiacono Fabio, Bielli Stefano
 * 
 */
public enum UserType {

	ADMIN {
		@Override
		public String toString() {
			return "admin";
		}
	},
	NORMAL {
		@Override
		public String toString() {
			return "normal";
		}

	};

}
