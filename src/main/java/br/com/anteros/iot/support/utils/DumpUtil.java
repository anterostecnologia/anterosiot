package br.com.anteros.iot.support.utils;

import java.io.PrintStream;
import java.util.Arrays;

import br.com.anteros.core.utils.StringUtils;

public class DumpUtil {
	private final static int LINE_LEN = 16;

	public static void displayDualDump(byte[] ba) {
		displayDualDump(ba, null);
	}

	public static void displayDualDump(String str) {
		displayDualDump(str, null);
	}

	public static void displayDualDump(String str, int lpad) {
		displayDualDump(str.getBytes(), null, lpad);
	}

	public static void displayDualDump(String str, PrintStream ps) {
		displayDualDump(str.getBytes(), ps, 0);
	}

	public static void displayDualDump(byte[] ba, PrintStream ps) {
		displayDualDump(ba, ps, 0);
	}

	public static void displayDualDump(byte[] ba, PrintStream ps, int lpad) {
		PrintStream out = (ps != null ? ps : System.out);
		String[] sa = DumpUtil.dualDump(ba, lpad);
		if (sa != null) {
			Arrays.stream(sa).forEach(str -> out.println(String.format("%s%s", lpad == 0 ? "\t" : pad(lpad), str)));
		}
	}

	public static String[] dualDump(String str) {
		byte[] ba = str.getBytes();
		return dualDump(ba);
	}

	private static String pad(int len) {
		String pad = "";
		if (len > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i=0; i<len; i++) {
				sb.append(" ");
			}
			pad = sb.toString();
		}
		return pad;
	}

	private static String separator() {
		String sep = "---+-";
		for (int i=0; i<LINE_LEN; i++) {
			sep += "---";
		}
		sep += "-+--";
		for (int i=0; i<LINE_LEN; i++) {
			sep += "-";
		}
		return sep;
	}

	/*
	 * Readable + HexASCII code.
	 * @see LINE_LEN member
	 */
	public static String[] dualDump(byte[] ba) {
		return dualDump(ba, 0);
	}

	public static String[] dualDump(byte[] ba, int lpad) {
		if (ba == null || ba.length == 0) {
			return new String[0];
		}

		int dim = ba.length / LINE_LEN;
		String[] result = new String[dim + 5]; // 2 first lines are labels

		int lineIdx = 0;
		result[lineIdx++] = separator();

		String first = "   | ";
		for (int i=0; i<LINE_LEN; i++) {
			first += (StringUtils.leftPad(Integer.toHexString(i & 0xFF).toUpperCase(), 2, " ") + " ");
		}
		first += " |";
		result[lineIdx++] = first;

		result[lineIdx++] = separator();

		for (int l = 0; l < (dim + 1); l++) {
			String lineLeft = (StringUtils.leftPad(Integer.toHexString(l & 0xFF).toUpperCase(), 2, '0') + " | ");
			String lineRight = "";
			int start = l * LINE_LEN;
			for (int c = start; c < Math.min(start + LINE_LEN, ba.length); c++) {
				lineLeft += (StringUtils.leftPad(Integer.toHexString(ba[c] & 0xFF).toUpperCase(), 2, "0") + " ");
				lineRight += (isAsciiPrintable((char) ba[c]) ? (char) ba[c] : ".");
			}
			lineLeft = StringUtils.rightPad(lineLeft, (3 * LINE_LEN) + 5, " ");
			result[lineIdx++] /*[l + 3]*/ = lineLeft + " |  " + lineRight;
		}
		result[lineIdx++] = separator();

		return result;
	}

	public static String dumpHexMess(byte[] mess) {
		String line = "";
		for (int i = 0; i < mess.length; i++)
			line += (StringUtils.leftPad(Integer.toHexString(mess[i] & 0xFF).toUpperCase(), 2, "0") + " ");
		return line.trim();
	}

	/**
	 * Might not work with some encodings...
	 *
	 * @param ch The character to test
	 * @return true when printable.
	 */
	public static boolean isAsciiPrintable(char ch) {
		return ch >= 32 && ch < 127;
	}

	public static void main(String... args) {
		String forTests = "$GPGSA,A,3,07,17,30,11,28,13,01,19,,,,,2.3,1.4,1.9*3D";
		String[] dd = dualDump(forTests);
		for (String l : dd) {
			System.out.println(l);
		}
	}
}
