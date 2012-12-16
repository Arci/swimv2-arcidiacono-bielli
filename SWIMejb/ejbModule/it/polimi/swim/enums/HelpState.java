package it.polimi.swim.enums;

/**
 * This class define the possible states of an help request
 * 
 * @author Arcidiacono Fabio, Bielli Stefano
 * 
 */
public enum HelpState {

	PENDING {
		@Override
		public String toString() {
			return "pending";
		}
	},
	CLOSED {
		@Override
		public String toString() {
			return "closed";
		}

	},
	OPEN {
		@Override
		public String toString() {
			return "open";
		}

	};

}
