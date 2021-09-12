package round1.problema1.weaktyping;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2021/round-1/problems/A1
 * @author atanasg
 */
public class WeakTypingSolver {

	private static final Path RES_FOLDER = Paths.get("res", "round1", "problema1", "weaktyping");

	private static int numOfCases;
	private static List<String> outputLines;

	private static void initStaticVariables(int num) {
		numOfCases = num;
		outputLines = new LinkedList<String>();
	}

	private static void writeOutputToFile(Path file) throws IOException {
		// force UNIX line endings
		String defaultSystemSeparator = System.getProperty("line.separator");
		System.setProperty("line.separator", "\n");

		Files.write(file, outputLines);

		// set back default separator
		System.setProperty("line.separator", defaultSystemSeparator);
	}

	private static int calculateHandSwitches(int sizeN, String stringW) {
		int handSwitches = 0;

		// The letter F does not have any role as it does not
		// force Timmy to switch hands
		String reducedStringW = stringW.replace("F", "");
		
		if (reducedStringW.length() == 0 || reducedStringW.length() == 1) {
			return handSwitches;
		}
		
		for (int i = 1; i < reducedStringW.length(); i++) {
			// "X to O" or "O to X" switch 
			if (reducedStringW.charAt(i) != reducedStringW.charAt(i - 1)) {
				handSwitches++;
			}
		}
		return handSwitches;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			int sizeN = Integer.parseInt(buff.readLine());
			String stringW = buff.readLine();

			int numOfHandSwitches = calculateHandSwitches(sizeN, stringW);
			outputLines.add("Case #" + (i + 1) + ": " + numOfHandSwitches);
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
