package com.everyware.posix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.everyware.util.StringUtils;
import com.everyware.util.TextWrap;

public class Getopt {
	public static final int MAX_LENGTH = 24;
	public static final int MIN_LENGTH = 18;
	public static final int LINE_WIDTH = 74;

	private List<Argument> definitions;
	private Map<Character, Argument> shortOptions;
	private Map<String, Argument> longOptions;
	private Map<String, Argument> options;
	private Map<String, List<String>> values;
	private String[] remaining;
	private boolean arbitrary;

	public Getopt() {
		definitions = new ArrayList<>();
		shortOptions = new HashMap<>();
		longOptions = new HashMap<>();
		options = new HashMap<>();
		values = null;
		remaining = null;
		arbitrary = true;
	}

	public void addValue(String id, char shortName, String longName, String help) {
		Argument arg = new Argument(id, true, shortName, longName, help);
		definitions.add(arg);
		shortOptions.put(shortName, arg);
		longOptions.put(longName, arg);
		options.put(id, arg);
	}

	public void addValue(String id, char shortName, String help) {
		Argument arg = new Argument(id, true, shortName, help);
		definitions.add(arg);
		shortOptions.put(shortName, arg);
		options.put(id, arg);
	}

	public void addValue(String id, String longName, String help) {
		Argument arg = new Argument(id, true, longName, help);
		definitions.add(arg);
		longOptions.put(longName, arg);
		options.put(id, arg);
	}

	public void addFlag(String id, char shortName, String longName, String help) {
		Argument arg = new Argument(id, false, shortName, longName, help);
		definitions.add(arg);
		shortOptions.put(shortName, arg);
		longOptions.put(longName, arg);
		options.put(id, arg);
	}

	public void addFlag(String id, char shortName, String help) {
		Argument arg = new Argument(id, false, shortName, help);
		definitions.add(arg);
		shortOptions.put(shortName, arg);
		options.put(id, arg);
	}

	public void addFlag(String id, String longName, String help) {
		Argument arg = new Argument(id, false, longName, help);
		definitions.add(arg);
		longOptions.put(longName, arg);
		options.put(id, arg);
	}

	public void setArbitraryArguments(boolean enabled) {
		arbitrary = enabled;
	}

	public void parseJava(String[] args) {
		parse(args, 0);
	}

	public void parse(String[] args) {
		parse(args, 1);
	}

	public void parse(String[] args, int start) {
		values = new HashMap<>();
		remaining = new String[0];
		LinkedList<Argument> valueArgs = new LinkedList<>();
		int i;
		for(i = start; i < args.length; i++) {
			String arg = args[i];
			if(valueArgs.size() > 0) {
				Argument def = valueArgs.removeFirst();
				addValue(def, arg);
			} else if(arg.equals("--") || arg.length() < 2) {
				if(arg.equals("--")) {
					remaining = new String[args.length - i - 1];
					i++;
				} else {
					remaining = new String[args.length - i];
				}
				for(int j = 0; i < args.length; i++, j++) {
					remaining[j] = args[i];
				}
				break;
			} else if(arg.charAt(0) == '-' && arg.charAt(1) != '-') {
				// single letter option(s)
				for(int j = 1; j < arg.length(); j++) {
					char ch = arg.charAt(j);
					Argument def = shortOptions.get(ch);
					if(def == null) {
						throw new InvalidOptionException(Character.toString(ch));
					}
					if(def.value) {
						valueArgs.add(def);
					} else {
						addFlag(def);
					}
				}
			} else if(arg.charAt(0) == '-' && arg.charAt(1) == '-') {
				String longName = arg.substring(2);
				Argument def = longOptions.get(longName);
				if(def == null) {
					throw new InvalidOptionException(longName);
				}
				if(def.value) {
					valueArgs.add(def);
				} else {
					addFlag(def);
				}
			} else {
				remaining = new String[args.length - i];
				for(int j = 0; i < args.length; i++, j++) {
					remaining[j] = args[i];
				}
				break;
			}
		}
		if(valueArgs.size() > 0) {
			throw new IllegalArgumentException("missing argument(s)");
		}
		if(!arbitrary && remaining.length > 0) {
			throw new ExtraOperandException(remaining[0]);
		}
	}

	private void addFlag(Argument arg) {
		addValue(arg, "");
	}

	private void addValue(Argument arg, String value) {
		List<String> args = values.get(arg.getId());
		if(args == null) {
			args = new ArrayList<>();
			values.put(arg.getId(), args);
		}
		args.add(value);
	}

	public List<String> getArgument(String id) {
		Argument def = options.get(id);
		if(def == null) {
			throw new IllegalArgumentException("unknown option: " + id);
		}
		List<String> value = values.get(id);
		if(value == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(value);
		}
	}

	public int getFlag(String id) {
		Argument def = options.get(id);
		if(def == null) {
			throw new IllegalArgumentException("unknown option: " + id);
		}
		if(def.value) {
			throw new IllegalArgumentException("the option " + id + " is not a flag");
		}
		List<String> args = getArgument(id);
		return args.size();
	}

	public boolean hasFlag(String id) {
		return getFlag(id) > 0;
	}

	public String[] getRemaining() {
		return remaining;
	}

	public String getHelp() {
		StringBuilder buf = new StringBuilder();
		int maxLong = 0;
		for(Argument def : definitions) {
			if(def.longName != null && def.longName.length() > maxLong) {
				maxLong = def.longName.length();
			}
		}
		if(maxLong > MAX_LENGTH) {
			maxLong = MAX_LENGTH;
		}
		if(maxLong < MIN_LENGTH) {
			maxLong = MIN_LENGTH;
		}
		int indent = maxLong + 11;
		boolean addN = false;
		for(Argument def : definitions) {
			if(addN) {
				buf.append('\n');
			} else {
				addN = true;
			}
			buf.append("  ");
			if(def.shortName != null) {
				buf.append("-").append(def.shortName);
			} else {
				buf.append("  ");
			}
			if(def.longName != null) {
				if(def.shortName != null) {
					buf.append(", ");
				} else {
					buf.append("  ");
				}
				buf.append("--");
				if(def.longName.length() > maxLong) {
					buf.append(def.longName);
					buf.append("\n");
					buf.append(StringUtils.repeat(" ", indent));
				}
				buf.append(StringUtils.padWithOverflow(def.longName, maxLong));
				buf.append(" ");
			} else if(def.shortName != null) {
				buf.append(StringUtils.repeat(" ", maxLong + 3));
			}
			String help = TextWrap.wrap(def.help, LINE_WIDTH - indent + 2,
					"\n" + StringUtils.repeat(" ", indent), 2, false);
			buf.append(help);
		}
		return buf.toString();
	}
}
