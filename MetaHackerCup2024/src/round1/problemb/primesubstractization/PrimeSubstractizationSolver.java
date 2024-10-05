package round1.problemb.primesubstractization;

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
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2024/round-1/problems/B
 * @author atanasg
 */
public class PrimeSubstractizationSolver {

	private static final Path RES_FOLDER = Paths.get("res", "round1", "problemb", "primesubstractization");

	private static final int MAX_N = 10_000_000;
	private static final boolean[] IS_PRIME = new boolean[MAX_N + 1];
	private static final Map<Integer, Integer> primeToNumSubstractizationsMap = new HashMap<>();
	
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
	

	private static void initPrimes() {
	    for (int i = 2; i <= MAX_N; i++) {
	    	IS_PRIME[i] = true;
	    }

	    for (int num = 2; num * num <= MAX_N; num++) {
	        if (IS_PRIME[num]) {
	            for (int j = num; num * j <= MAX_N; j++) {
	            	IS_PRIME[num * j] = false;
	            }
	        }
	    }
	}
	
	private static void initNPrimesSubstractizations() {
		primeToNumSubstractizationsMap.put(5, 2);
		Set<Integer> currentPrimeSubstractizationSet = new HashSet<>();
		currentPrimeSubstractizationSet.add(3);
		currentPrimeSubstractizationSet.add(2);
		
		List<Integer> primesToConsider = new LinkedList<>();
		primesToConsider.add(2);
		primesToConsider.add(3);
		primesToConsider.add(5);
		
		for (int i = 7; i <= MAX_N; i = i + 2) {
			if(IS_PRIME[i]) {
				// try to find the new substractizations
				for (int p : primesToConsider) {
					if (i - p < primesToConsider.get(primesToConsider.size() - 1)) {
						// smaller number coming and they are possibly in the set
						break;
					}
					if (IS_PRIME[i - p]) {
						if (currentPrimeSubstractizationSet.contains(i - p)) {
							// this and possible next primes are already contained in the set
							break;
						} else {
							currentPrimeSubstractizationSet.add(i - p);
						}
					}
				}
				
				//prepare next iteration
				primeToNumSubstractizationsMap.put(i, currentPrimeSubstractizationSet.size());
				primesToConsider.add(i);
			}
		}
		
	}
	
	private static int calculateNumberOfNPrimeSubstractizations(int n) {
		if (n <= 4) {
			// no prime substractizations
			return 0;
		} else if (IS_PRIME[n]) {
			return primeToNumSubstractizationsMap.get(n);
		} else {
			while (!IS_PRIME[n]) {
				n--;
			}
			return primeToNumSubstractizationsMap.get(n);
		}
	}

	public static void main(String[] args) throws IOException {
		//pre-work
		initPrimes();
		initNPrimesSubstractizations();

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String params = buff.readLine();
			int n = Integer.parseInt(params);

			outputLines.add("Case #" + (i + 1) + ": " + calculateNumberOfNPrimeSubstractizations(n));
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
