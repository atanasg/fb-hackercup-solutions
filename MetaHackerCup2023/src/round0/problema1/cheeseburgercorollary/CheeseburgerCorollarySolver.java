package round0.problema1.cheeseburgercorollary;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2023/practice-round/problems/A1
 * @author atanasg
 */
public class CheeseburgerCorollarySolver {
	
	private static final Path RES_FOLDER = Paths.get("res", "round0", "problema1", "cheeseburgercorollary");
	
	private static final String WHITESPACE = " ";
	private static final String TRUE_CASE = "YES";
	private static final String FALSE_CASE = "NO";

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
	

	private static String determineIfBurgesAreEnoughToBuildKdeckerBurger(int singleBurgers, int doubleBurgers, int targetKdecker) {
		if (singleBurgers == 0 && doubleBurgers == 0) {
			// nothing can be built without burgers
			return FALSE_CASE;
		}
		
		int availableBuns = 2 * (singleBurgers + doubleBurgers);
		int requiredBuns = targetKdecker + 1;
		
		if (requiredBuns > availableBuns) {
			return FALSE_CASE;
		}
		
		int availableCheeseAndPatties = singleBurgers + 2 * doubleBurgers;
		int requiredCheeseAndPatties = targetKdecker;
		
		if (requiredCheeseAndPatties > availableCheeseAndPatties) {
			return FALSE_CASE;
		}
		
		return TRUE_CASE;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String testCaseInput = buff.readLine();
			String[] testCaseArray = testCaseInput.split(WHITESPACE);
			int s = Integer.parseInt(testCaseArray[0]);
			int d = Integer.parseInt(testCaseArray[1]);
			int k = Integer.parseInt(testCaseArray[2]);
			
			String yesOrNo = determineIfBurgesAreEnoughToBuildKdeckerBurger(s, d, k);
			outputLines.add("Case #" + (i + 1) + ": " + yesOrNo);
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
