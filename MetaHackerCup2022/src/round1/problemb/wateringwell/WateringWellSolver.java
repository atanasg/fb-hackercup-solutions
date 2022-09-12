package round1.problemb.wateringwell;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2022/round-1/problems/B1
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2022/round-1/problems/B2 (works, but with poor performance)
 * @author atanasg
 */

class Pair {
	int x;
	int y;
	
	public Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "Pair [x=" + x + ", y=" + y + "]";
	}
}

public class WateringWellSolver {
	
	private static final Path RES_FOLDER = Paths.get("res", "round1", "problemb", "wateringwell");

	private static final int MODULO_CONSTANT = 1_000_000_007;
	private static final String WHITESPACE = " ";

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
	
	private static int calculateInconveniences(Pair[] trees, Pair[] wells) {
		long sum = 0;
		
		long startOfMaps = System.currentTimeMillis();
		Map<Integer, Integer> numTreesAtSpecificX = new HashMap<>();
		Map<Integer, Integer> numTreesAtSpecificY = new HashMap<>();
		
		for(Pair t : trees) {
			numTreesAtSpecificX.merge(t.x, 1, Integer::sum);
			numTreesAtSpecificY.merge(t.y, 1, Integer::sum);
		}
		long endOfmaps = System.currentTimeMillis();
		System.out.println("Maps created: " + (endOfmaps - startOfMaps) + "ms.");
		

		long startOfWellCalucations = System.currentTimeMillis();

		for(int i = 0; i < wells.length; i++) {
			Pair w = wells[i];
			for(Entry<Integer, Integer> e : numTreesAtSpecificX.entrySet()) {
				int xValue = e.getKey();
				int numOccurences = e.getValue();
				int diffX = Math.abs(xValue - w.x);
				long quadDiffX = 1L * diffX * diffX;
				long modquadDiffX = quadDiffX % MODULO_CONSTANT;
				long currentSum = (numOccurences * modquadDiffX) % MODULO_CONSTANT;
				sum = (sum + currentSum) % MODULO_CONSTANT;
			}
			
			for(Entry<Integer, Integer> e : numTreesAtSpecificY.entrySet()) {
				int yValue = e.getKey();
				int numOccurences = e.getValue();
				int diffY = Math.abs(yValue - w.y);
				long quadDiffY = 1L * diffY * diffY;
				long modquadDiffY = quadDiffY % MODULO_CONSTANT;
				long currentSum = (numOccurences * modquadDiffY) % MODULO_CONSTANT;
				sum = (sum + currentSum) % MODULO_CONSTANT;
			}
		}
		long endOfWellCalucations = System.currentTimeMillis();
		System.out.println("Wells distances calculated: " + (endOfWellCalucations - startOfWellCalucations) + "ms.");
		
		return (int) (sum % MODULO_CONSTANT);
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			long startOfReading = System.currentTimeMillis();
			int numTrees = Integer.parseInt(buff.readLine());
			Pair[] trees = new Pair[numTrees];
			for(int k = 0; k < numTrees; k++) {
				String currentTreeString = buff.readLine();
				String[] currentTreeCoords = currentTreeString.split(WHITESPACE);
				Pair currentTree = new Pair(Integer.parseInt(currentTreeCoords[0]), Integer.parseInt(currentTreeCoords[1]));
				trees[k] = currentTree;
			}
			
			int numWells = Integer.parseInt(buff.readLine());
			Pair[] wells = new Pair[numWells];
			for(int k = 0; k < numWells; k++) {
				String currentWellString = buff.readLine();
				String[] currentWellCoords = currentWellString.split(WHITESPACE);
				Pair currentWell = new Pair(Integer.parseInt(currentWellCoords[0]), Integer.parseInt(currentWellCoords[1]));
				wells[k] = currentWell;
			}
			long endOfReading = System.currentTimeMillis();
			
			
			System.out.println("Case " + (i +1) + " input processed for " + (endOfReading-startOfReading) + "ms.");
			System.out.println("Num trees: " + numTrees);
			System.out.println("Num wells: " + numWells);
			long currentTimeStartCalc = System.currentTimeMillis();
			int inconveniencesSum = calculateInconveniences(trees, wells);
			long currentTimeEndCalc = System.currentTimeMillis();
			System.out.println((currentTimeEndCalc-currentTimeStartCalc) + "ms. Calculation done, moving to next case...");
			System.out.println("---");
			outputLines.add("Case #" + (i + 1) + ": " + inconveniencesSum);
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
