package round1.problema2.weaktyping;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2021/round-1/problems/A2
 * @author atanasg
 */
public class WeakTypingA2Solver {

	private static final Path RES_FOLDER = Paths.get("res", "round1", "problema2", "weaktyping");

	private static int MODULO_CONSTANT = 1_000_000_007;

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

	private static int calculateHandSwitchesOfAllSubstrings(int sizeN, String stringW) {
		int handSwitches = 0;
		
		if (stringW.length() == 1 || stringW.indexOf("X") == -1 || stringW.indexOf("O") == -1) {
			return handSwitches;
		}
		
		// init writing hand
		boolean leftHandWriting = true;
		for (int i = 0; i < stringW.length(); i++) {
			if (stringW.charAt(i) == 'F') {
				continue;
			} else if (stringW.charAt(i) == 'X') {
				leftHandWriting = true;
				break;
			} else {
				leftHandWriting = false;
				break;
			}
		}
		
		for (int i = 1; i < stringW.length(); i++) {
			// hand switch needed 
			if ((stringW.charAt(i) == 'O' && leftHandWriting) ||
					(stringW.charAt(i) == 'X' && !leftHandWriting)) {
				// switch hands
				leftHandWriting = !leftHandWriting;
				// calculate how many substrings include this switch
				int preceedingFs = 0;
				for(int j = i - 1; j >= 0; j--) {
					if (stringW.charAt(j) == 'F') {
						preceedingFs++;
					} else {
						break;
					}
				}
				long numSubstrings = 1L * (i - preceedingFs) * (sizeN - i);
				// add to total sum and apply modulo
				handSwitches = (int) ((handSwitches + numSubstrings) % MODULO_CONSTANT);
			}
		}
		return handSwitches % MODULO_CONSTANT;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			int sizeN = Integer.parseInt(buff.readLine());
			String stringW = buff.readLine();

			int numOfHandSwitches = calculateHandSwitchesOfAllSubstrings(sizeN, stringW);
			outputLines.add("Case #" + (i + 1) + ": " + numOfHandSwitches);
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
