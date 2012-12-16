package it.polimi.swim.enums;

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
