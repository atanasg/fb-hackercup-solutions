package round1.problemb1.sum41;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2023/round-1/problems/B1
 * @author atanasg
 */
public class Sum41Solver {
	
	private static final Path RES_FOLDER = Paths.get("res", "round1", "problemb1", "sum41");
	
	private static final String WHITESPACE = " ";
	private static final String NO_SOLUTION = "-1";

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
	
	/**
	 * It does not make sense to try factors 41 and 40 as the other factors can be only nothing or 1 in order to have
	 * sum of factors exactly 41.
	 * 
	 * @param number
	 * @return possible correct factorisation of the number with factors up to 39
	 */
	private static List<Integer> factorsUpTo39(int number) {
		List<Integer> factors = new LinkedList<>();

		int n = number;
		for (int i = 2; i <= 39; i++) {
			while (n % i == 0) {
				factors.add(i);
				n /= i;
			}
		}
		return factors;
	}

	/**
	 * 
	 * @param solution a list of positive integers
	 * @return the expected string in the format: {@code <size> int1 int2 int3 ... intN}
	 */
	private static String createFormattedSolutionString(List<Integer> solution) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(solution.size());
		sb.append(WHITESPACE);
		
		for(int i : solution) {
			sb.append(i);
			sb.append(WHITESPACE);
		}
		
		// trim needed because of trailing whitespace
		return sb.toString().trim();
	}

	private static String findArrayWithSum41AndProductP(int product) {
		if (product < 41) {
			// create a solution "1 1 ... 1 <product>" with as many "1" as needed so that the following are true:
			// product = product * 1 * ... * 1;
			// 41 = product + 1 + 1 ... + 1;
			List<Integer> solution = new LinkedList<>();
			
			for (int i = 0; i < (41 - product); i++) {
				solution.add(1);
			}
			solution.add(product);
			
			return createFormattedSolutionString(solution);
		} else if (product == 41) {
			return "1 41";
		} else {
			List<Integer> possibleSolution = factorsUpTo39(product);
			
			int resultingProduct = 1;
			for (int p : possibleSolution) {
				resultingProduct = resultingProduct * p;
			}
			
			if (resultingProduct != product) {
				return NO_SOLUTION;
			}
			
			int resultingSum = possibleSolution.stream().mapToInt(i -> i).sum();
			if (resultingSum > 41) {
				return NO_SOLUTION;
			}
			
			List<Integer> solution = new LinkedList<>();
			solution.addAll(possibleSolution);
			
			for (int i = 0; i < (41 - resultingSum); i++) {
				solution.add(1);
			}
			Collections.sort(solution);
			return createFormattedSolutionString(solution);
		}
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String testCaseInput = buff.readLine();
			int p = Integer.parseInt(testCaseInput);
			
			String solutionArrayString = findArrayWithSum41AndProductP(p);
			outputLines.add("Case #" + (i + 1) + ": " + solutionArrayString);
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
