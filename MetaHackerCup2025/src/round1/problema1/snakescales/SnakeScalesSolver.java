package round1.problema1.snakescales;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2025/round-1/problems/A1
 * @author atanasg
 */
public class SnakeScalesSolver {

	private static final Path RES_FOLDER = Paths.get("res", "round1", "problema1", "snakescales");

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
	
	private static int calculateShortestLadderLength(int n, int[] platformsHeights) {
		// no ladder needed for single platform
		if (n == 1) {
			return 0;
		}
		
		int shortestRequiredLadderLength = 0;

		for (int i = 1; i < n; i++) {
			int currentPlatformHeight = platformsHeights[i];
			int previosPlatformHeight = platformsHeights[i - 1];
			
			int ladderLengthNeeded = Math.abs(previosPlatformHeight - currentPlatformHeight);
			
			if (ladderLengthNeeded > shortestRequiredLadderLength) {
				shortestRequiredLadderLength = ladderLengthNeeded;
			}
		}
		return shortestRequiredLadderLength;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			int n = Integer.parseInt(buff.readLine());
			
			String platformsHeightsString = buff.readLine();
			String[] platformsHeightsArray = platformsHeightsString.split(" ");
			
			int[] platformsHeights = new int[n];
			
			for(int j = 0; j < n; j++) {
				platformsHeights[j] = Integer.parseInt(platformsHeightsArray[j]);
			}

			outputLines.add("Case #" + (i + 1) + ": " + calculateShortestLadderLength(n, platformsHeights));
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
