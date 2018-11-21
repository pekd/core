package com.everyware.posix.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.everyware.posix.ExtraOperandException;
import com.everyware.posix.Getopt;
import com.everyware.posix.InvalidOptionException;

public class GetoptTest {
	private Getopt getopt;

	@Before
	public void setup() {
		getopt = new Getopt();
	}

	private void addUnameFlags() {
		getopt.addFlag("all", 'a', "all",
				"print all information, in the following order, except omit -p and -i if unknown:");
		getopt.addFlag("kname", 's', "kernel-name", "print the kernel name");
		getopt.addFlag("nodename", 'n', "nodename", "print the network node hostname");
		getopt.addFlag("krel", 'r', "kernel-release", "print the kernel release");
		getopt.addFlag("kver", 'v', "kernel-version", "print the kernel version");
		getopt.addFlag("machine", 'm', "machine", "print the machine hardware name");
		getopt.addFlag("processor", 'p', "processor", "print the processor type (non-portable)");
		getopt.addFlag("hwplatform", 'i', "hardware-platform", "print the hardware platform (non-portable)");
		getopt.addFlag("os", 'o', "operating-system", "print the operating system");
		getopt.addFlag("help", "help", "display this help and exit");
		getopt.addFlag("version", "version", "output version information and exit");
		getopt.setArbitraryArguments(false);
	}

	@Test
	public void testHelp1() {
		addUnameFlags();
		String act = getopt.getHelp();
		String ref = "  -a, --all                print all information, in the following order,\n" +
				"                             except omit -p and -i if unknown:\n" +
				"  -s, --kernel-name        print the kernel name\n" +
				"  -n, --nodename           print the network node hostname\n" +
				"  -r, --kernel-release     print the kernel release\n" +
				"  -v, --kernel-version     print the kernel version\n" +
				"  -m, --machine            print the machine hardware name\n" +
				"  -p, --processor          print the processor type (non-portable)\n" +
				"  -i, --hardware-platform  print the hardware platform (non-portable)\n" +
				"  -o, --operating-system   print the operating system\n" +
				"      --help               display this help and exit\n" +
				"      --version            output version information and exit";
		assertEquals(ref, act);
	}

	@Test
	public void testFlags1() {
		addUnameFlags();
		String[] args = new String[] { "-", "-s", "-n", "-r" };
		getopt.parse(args);
		assertFalse(getopt.hasFlag("all"));
		assertTrue(getopt.hasFlag("kname"));
		assertTrue(getopt.hasFlag("nodename"));
		assertTrue(getopt.hasFlag("krel"));
		assertFalse(getopt.hasFlag("kver"));
		assertFalse(getopt.hasFlag("machine"));
		assertFalse(getopt.hasFlag("processor"));
		assertFalse(getopt.hasFlag("hwplatform"));
		assertFalse(getopt.hasFlag("os"));
		assertFalse(getopt.hasFlag("help"));
		assertFalse(getopt.hasFlag("version"));
		assertArrayEquals(new String[] {}, getopt.getRemaining());
	}

	@Test(expected = InvalidOptionException.class)
	public void testInvalidOption1() {
		addUnameFlags();
		String[] args = new String[] { "-", "-h" };
		getopt.parse(args);
	}

	@Test(expected = InvalidOptionException.class)
	public void testInvalidOption2() {
		addUnameFlags();
		String[] args = new String[] { "-", "--Help" };
		getopt.parse(args);
	}

	@Test(expected = ExtraOperandException.class)
	public void testExtraOperand1() {
		addUnameFlags();
		String[] args = new String[] { "-", "" };
		getopt.parse(args);
	}

	@Test(expected = ExtraOperandException.class)
	public void testExtraOperand2() {
		addUnameFlags();
		String[] args = new String[] { "-", "test" };
		getopt.parse(args);
	}

	@Test(expected = ExtraOperandException.class)
	public void testExtraOperand3() {
		addUnameFlags();
		String[] args = new String[] { "-", "test", "value" };
		getopt.parse(args);
	}
}
