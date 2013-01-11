package it.polimi.swim.enums;

/**
 * This class define the states for both friendship requests and ability
 * requests
 * 
 * @author Arcidiacono Fabio, Bielli Stefano
 * 
 */
public enum RequestState {

	PENDING {
		@Override
		public String toString() {
			return "pending";
		}
	},
	REJECTED {
		@Override
		public String toString() {
			return "rejected";
		}
	},
	ACCEPTED {
		@Override
		public String toString() {
			return "accepted";
		}
	};

}
