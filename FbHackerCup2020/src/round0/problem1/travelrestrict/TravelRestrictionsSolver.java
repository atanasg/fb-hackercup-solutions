package round0.problem1.travelrestrict;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2020/qualification-round/problems/A
 * @author atanasg
 */
public class TravelRestrictionsSolver {

	private static final Path RES_FOLDER = Paths.get("res", "round0", "problem1", "travelrestrict");

	private static final char REACHABLE = 'Y';
	private static final char NOT_REACHABLE = 'N';

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

	private static List<String> calculateReachableAirportsMatrix(int numAirports, String inboundRules,
			String outboundRules) {
		char[][] reachableAirportsMatrix = initReachableAirportsMatrix(numAirports);

		Map<Integer, Set<Integer>> airportsDirectedGraph = initAirportsDirectedGraphWithoutRestrictedConnections(
				numAirports);

		applyInboundAndOutboundRules(numAirports, inboundRules, outboundRules, airportsDirectedGraph);

		// compute reachability
		for (int i = 1; i <= numAirports - 1; i++) {
			if (airportsDirectedGraph.get(i).contains(i - 1)) {
				// new airport can reach previous one and all its transitive airports
				reachableAirportsMatrix[i][i - 1] = REACHABLE;
				for (int j = 0; j <= i - 2; j++) {
					if (reachableAirportsMatrix[i - 1][j] == REACHABLE) {
						reachableAirportsMatrix[i][j] = REACHABLE;
					}
				}
			}

			if (airportsDirectedGraph.get(i - 1).contains(i)) {
				// previous airport and all that can reach previous one can reach current as well
				reachableAirportsMatrix[i - 1][i] = REACHABLE;
				for (int j = 0; j <= i - 2; j++) {
					if (reachableAirportsMatrix[j][i - 1] == REACHABLE) {
						reachableAirportsMatrix[j][i] = REACHABLE;
					}
				}
			}
		}

		return convertToListOfStrings(reachableAirportsMatrix);
	}

	/**
	 * Example init matrix: YNN NYN NNY
	 *
	 * @param numAirports
	 * @return
	 */
	private static char[][] initReachableAirportsMatrix(int numAirports) {
		char[][] reachableAirportsMatrix = new char[numAirports][numAirports];

		for (int i = 0; i < reachableAirportsMatrix.length; i++) {
			for (int j = 0; j < reachableAirportsMatrix.length; j++) {
				reachableAirportsMatrix[i][j] = NOT_REACHABLE;
				if (i == j) {
					// every airport is reachable by itself by definition
					reachableAirportsMatrix[i][j] = REACHABLE;
				}
			}
		}
		return reachableAirportsMatrix;
	}

	/**
	 * Example graph without restrictions/rules: {0=[1], 1=[0, 2], 2=[1, 3], 3=[2,
	 * 4], 4=[3]}
	 *
	 * @param numAirports
	 * @return
	 */
	private static Map<Integer, Set<Integer>> initAirportsDirectedGraphWithoutRestrictedConnections(int numAirports) {
		// First airport has index 0 and last airport has index numAirports-1
		Map<Integer, Set<Integer>> airportsDirectedGraph = new HashMap<>();

		Set<Integer> connectionToSecondAirport = new HashSet<>();
		connectionToSecondAirport.add(1);
		airportsDirectedGraph.put(0, connectionToSecondAirport);

		Set<Integer> connectionToNextToLastAirport = new HashSet<>();
		connectionToNextToLastAirport.add(numAirports - 2);
		airportsDirectedGraph.put(numAirports - 1, connectionToNextToLastAirport);

		for (int i = 1; i <= numAirports - 2; i++) {
			Set<Integer> connectionForCurrentAirport = new HashSet<>();
			// connection to previous and next airport
			connectionForCurrentAirport.add(i - 1);
			connectionForCurrentAirport.add(i + 1);
			airportsDirectedGraph.put(i, connectionForCurrentAirport);
		}

		return airportsDirectedGraph;
	}

	private static void applyInboundAndOutboundRules(int numAirports, String inboundRules, String outboundRules,
			Map<Integer, Set<Integer>> airportsDirectedGraph) {
		for (int i = 0; i < numAirports; i++) {
			// inbound rules - no connections TO this airport
			if (inboundRules.charAt(i) == 'N') {
				if (i - 1 >= 0) {
					Set<Integer> connectionsOfIminus1 = airportsDirectedGraph.get(i - 1);
					connectionsOfIminus1.remove(i);
				}
				if (i + 1 <= numAirports - 1) {
					Set<Integer> connectionsOfIplus1 = airportsDirectedGraph.get(i + 1);
					connectionsOfIplus1.remove(i);
				}
			}

			// outbound rules - no connections FROM this airport
			if (outboundRules.charAt(i) == 'N') {
				airportsDirectedGraph.put(i, new HashSet<>());
			}
		}
	}

	private static List<String> convertToListOfStrings(char[][] reachableAirportsMatrix) {
		List<String> result = new LinkedList<>();

		for (int i = 0; i < reachableAirportsMatrix.length; i++) {
			result.add(new String(reachableAirportsMatrix[i]));
		}
		return result;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			int numAirports = Integer.parseInt(buff.readLine());
			String inboundRules = buff.readLine();
			String outboundRules = buff.readLine();

			List<String> reachableAirportsMatrix = calculateReachableAirportsMatrix(numAirports, inboundRules,
					outboundRules);
			outputLines.add("Case #" + (i + 1) + ": ");
			outputLines.addAll(reachableAirportsMatrix);
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}

}
