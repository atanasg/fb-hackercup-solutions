package round0.problem1.tourist;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @see https://www.facebook.com/hackercup/problem/1632703893518337/
 * @author atanasg
 */
public class TouristSolver {

	private static final Path RES_FOLDER = Paths.get("res", "round0", "problem1", "tourist");

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

	private static String computeAttractionsToVisitInLastVisit(String[] prioritySortedAttractionsNames,
			int totalAttractionsCount, int attractionsCountPerVisit, long numOfVisits) {
		final StringBuilder result = new StringBuilder("");

		int startingIndexForLastVisit = (int) (((numOfVisits - 1) * attractionsCountPerVisit) % totalAttractionsCount);

		List<Integer> indicesOfAttractionsInLastVisit = IntStream
				.iterate(startingIndexForLastVisit, (i) -> (i + 1) % totalAttractionsCount)
				.limit(attractionsCountPerVisit).mapToObj(i -> i).collect(Collectors.toList());

		// respect priority for output
		Collections.sort(indicesOfAttractionsInLastVisit);

		// assemble result string
		indicesOfAttractionsInLastVisit.stream()
				.forEach(i -> result.append(prioritySortedAttractionsNames[i]).append(" "));

		// remove last space
		result.deleteCharAt(result.length() - 1);

		return result.toString();
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String caseParameters = buff.readLine();
			String[] caseParamatersSplitted = caseParameters.split(" ");

			int totalAttractionsCount = Integer.parseInt(caseParamatersSplitted[0]);
			int attractionsCountPerVisit = Integer.parseInt(caseParamatersSplitted[1]);
			long numOfVisits = Long.parseLong(caseParamatersSplitted[2]);

			String[] prioritySortedAttractionsNames = new String[totalAttractionsCount];
			for (int k = 0; k < totalAttractionsCount; k++) {
				prioritySortedAttractionsNames[k] = buff.readLine();
			}

			String attractionsToVisitInLastVisit = computeAttractionsToVisitInLastVisit(prioritySortedAttractionsNames,
					totalAttractionsCount, attractionsCountPerVisit, numOfVisits);
			outputLines[i] = "Case #" + (i + 1) + ": " + attractionsToVisitInLastVisit;
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}

}
