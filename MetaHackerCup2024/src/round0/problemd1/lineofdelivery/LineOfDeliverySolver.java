package round0.problemd1.lineofdelivery;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2024/practice-round/problems/D1
 * @author atanasg
 */
public class LineOfDeliverySolver {

	private static final Path RES_FOLDER = Paths.get("res", "round0", "problemd1", "lineofdelivery");

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
	
	private static String findClosestStone(int n, int g, int[] throwingEnergies) {
		// The end positions of the stones correspond exactly to the throwing energies. E.g. in case stones are thrown
		// with Ei = 7 1 3 the end positions will be exactly 1,3,7 regardless of the throwing order. Furthermore, the
		// first thrown stone will have the max distance (e.g. 7) and the last thrown stone will have the min distance
		// (e.g. 1). This is due to the fact that stones "do not overtake" each other, they just pass energy in case of
		// collision.
		Integer[] finalPositions = Arrays.stream(throwingEnergies)
				.boxed()
				.sorted(Collections.reverseOrder())
				.toArray(Integer[]::new);
		
		int closestStoneIndex = Integer.MIN_VALUE; // starting with 1
		int distanceToG = Integer.MAX_VALUE;
		
		if (g > finalPositions[0]) {
			// even the farthest stone has not reached G
			closestStoneIndex = 1;
			distanceToG = g - finalPositions[0];
		} else if (g < finalPositions[finalPositions.length - 1]) {
			// G is even closer than the closest stone
			closestStoneIndex = finalPositions.length;
			distanceToG = finalPositions[finalPositions.length - 1] - g;
		} else {
			// G is somewhere between the stones
			for(int i = 0 ; i < finalPositions.length; i ++) {
				if (finalPositions[i] == g) {
					// we have hit G
					closestStoneIndex = i + 1;
					distanceToG = 0;
					break;
				} else if (Math.abs(finalPositions[i] - g) < distanceToG){
					// we are getting closer
					closestStoneIndex = i + 1;
					distanceToG = Math.abs(finalPositions[i] - g);
				} else {
					// distance is not getting smaller (stays same or growing)
					break;
				}
			}
		}
		
		return closestStoneIndex + " " + distanceToG;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String params = buff.readLine();
			String[] paramsArray = params.split(" ");
			int n = Integer.parseInt(paramsArray[0]);
			int g = Integer.parseInt(paramsArray[1]);
			int[] throwingEnergies = new int[n];
			
			for(int j = 0; j < n; j++) {
				throwingEnergies[j] = Integer.parseInt(buff.readLine());
			}

			String result = findClosestStone(n, g, throwingEnergies);
			outputLines.add("Case #" + (i + 1) + ": " + result);
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
