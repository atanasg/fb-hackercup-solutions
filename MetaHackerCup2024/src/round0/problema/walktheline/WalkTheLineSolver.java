package round0.problema.walktheline;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2024/practice-round/problems/A
 * @author atanasg
 */
public class WalkTheLineSolver {

	private static final Path RES_FOLDER = Paths.get("res", "round0", "problema", "walktheline");
	
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
	
	private static String isPassingTheBridgeBelowKSecondsPossible(int n, int k, int[] travelTimes) {
		long minTotalTravelTime = Long.MAX_VALUE;
		
		if (n == 1) {
			// single traveller
			// Just needs to pass the bridge
			minTotalTravelTime = travelTimes[0];
		} else if (n == 2) {
			// two travellers
			// Need only one way (with the slower traveller riding on the wheelbarrow)
			minTotalTravelTime = Arrays.stream(travelTimes).min().getAsInt();
		} else {
			// multiple travellers
			// The fastest traveller (minPassingTime) will bring N-2 people to the other side and return back.
			// In the last pass the fastest traveller takes the last remaining person in the wheelbarrow and they pass
			// together. General formula: (N-2)*2*minPassingTime + minPassingTime
			int minPassingTime = Arrays.stream(travelTimes).min().getAsInt();
			minTotalTravelTime = (n - 2) * 2L * minPassingTime + minPassingTime;
		}
		
		if (minTotalTravelTime <= k) {
			return TRUE_CASE;
		} else {
			return FALSE_CASE;
		}
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String params = buff.readLine();
			String[] paramsArray = params.split(" ");
			int n = Integer.parseInt(paramsArray[0]);
			int k = Integer.parseInt(paramsArray[1]);
			int[] travelTimes = new int[n];
			
			for(int j = 0; j < n; j++) {
				travelTimes[j] = Integer.parseInt(buff.readLine());
			}

			String yesOrNo = isPassingTheBridgeBelowKSecondsPossible(n, k, travelTimes);
			outputLines.add("Case #" + (i + 1) + ": " + yesOrNo);
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
