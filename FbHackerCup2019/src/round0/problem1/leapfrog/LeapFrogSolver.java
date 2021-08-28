package round0.problem1.leapfrog;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2019/qualification-round/problems/A
 * @author atanasg
 */
public class LeapFrogSolver {

	private static final Path RES_FOLDER = Paths.get("res", "round0", "problem1", "leapfrog");

	private static final String YES = "Y";
	private static final String NO = "N";

	private static final char ALPHA_FROG = 'A';
	private static final char BETA_FROG = 'B';
	private static final char UNOCCUPIED = '.';

	private static int numOfCases;
	private static String[] outputLines;

	private static void initStaticVariables(int num) {
		numOfCases = num;
		outputLines = new String[numOfCases];
	}

	private static void writeOutputToFile(Path file) throws IOException {
		// force UNIX line endings
		String defaultSystemSeparator = System.getProperty("line.separator");
		System.setProperty("line.separator", "\n");

		Files.write(file, Arrays.asList(outputLines));

		// set back default separator
		System.setProperty("line.separator", defaultSystemSeparator);
	}

	private static String computeFrogCanReachTheRightmostSpot(String frogsCase) {
		// should start with alpha frog
		if (frogsCase.indexOf(ALPHA_FROG) != 0) {
			return NO;
		}

		// should have at least one place for jumping
		if (frogsCase.indexOf(UNOCCUPIED) == -1) {
			return NO;
		}

		// should contain at least one beta frog
		if (frogsCase.indexOf(BETA_FROG) == -1) {
			return NO;
		}

		// count and group the different types of fields
		Map<Character, Long> frogsDistribution = frogsCase.chars().mapToObj(i -> new Character((char) i))
				.collect(Collectors.groupingBy(c -> c, Collectors.counting()));

		// these are the least beta frogs needed:
		// AB.[B.]*, e.g. AB.B.B.B.B.B.
		// more frogs are OK as well, e.g. ABBB.BBBBBB.B.B.
		long numOfBetaFrogs = frogsDistribution.get(BETA_FROG);
		long numOfEmptySpaces = frogsDistribution.get(UNOCCUPIED);
		if (numOfBetaFrogs >= numOfEmptySpaces) {
			return YES;
		} else {
			return NO;
		}
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String frogsCase = buff.readLine();

			String frogCanReachTheRightmostSpot = computeFrogCanReachTheRightmostSpot(frogsCase);
			outputLines[i] = "Case #" + (i + 1) + ": " + frogCanReachTheRightmostSpot;
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}

}
