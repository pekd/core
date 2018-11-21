package com.everyware.posix;

public class Argument {
	public final boolean value;
	public final String id;
	public final Character shortName;
	public final String longName;
	public final String help;

	public Argument(String id, boolean value, char shortName, String longName, String help) {
		this.id = id;
		this.value = value;
		this.shortName = shortName;
		this.longName = longName;
		this.help = help;
	}

	public Argument(String id, boolean value, String longName, String help) {
		this.id = id;
		this.value = value;
		this.shortName = null;
		this.longName = longName;
		this.help = help;
	}

	public Argument(String id, boolean value, char shortName, String help) {
		this.id = id;
		this.value = value;
		this.shortName = shortName;
		this.longName = null;
		this.help = help;
	}

	public String getId() {
		return id;
	}

	public char getShortName() {
		return shortName;
	}

	public String getLongName() {
		return longName;
	}

	public String getHelp() {
		return help;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		if(shortName != null) {
			result.append('-').append(shortName);
		}
		if(longName != null) {
			if(shortName != null) {
				result.append(", ");
			}
			result.append("--").append(longName);
		}
		result.append(" ").append(help);
		return result.toString().trim();
	}
}
