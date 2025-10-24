package round1.problemb1.finalproduct;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2025/round-1/problems/B1
 * @author atanasg
 */
public class FinalProductSolver {

	private static final Path RES_FOLDER = Paths.get("res", "round1", "problemb1", "finalproduct");

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
	
	private static String calculateProductCoolness(int n, int a, int b) {
		int[] result = new int [2 * n];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = 1;
		}
		
		if (b == 1) {
			System.out.println("nothing to do for coolness 1");
		} else if(b == 2 || b == 3) {
			result[result.length - 1] = b;
	    } else if(isPrime(b)) {
			result[result.length - 1] = b;
		} else {
			int largestDiv = findLargestDivisor(b);
			int other = b / largestDiv;
			result[result.length - 1] = largestDiv;
			result[result.length - 2] = other;
		}
		
		String resultAsString = "";
		for (int i = 0; i < result.length; i++) {
			resultAsString = resultAsString + result[i];
			if (i <= result.length - 2) {
				resultAsString = resultAsString + " ";
			}
		}
		return resultAsString;
	}
	
	private static boolean isPrime(int number) {
        for (int i = 2; i <= number/2; i++) {
            if(number% i == 0) {
                return false;
            }
        }

        return true;
    }
	
	private static int findLargestDivisor(int n) {
	    for (int i = n / 2; i >= 2; i--) {
	        if (n % i == 0) {
	            return i;
	        }
	    }
	    return 1;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String paramsString = buff.readLine();
			String[] paramsArray = paramsString.split(" ");
			
			int n = Integer.parseInt(paramsArray[0]);
			int a = Integer.parseInt(paramsArray[1]);
			int b = Integer.parseInt(paramsArray[2]);

			outputLines.add("Case #" + (i + 1) + ": " + calculateProductCoolness(n, a, b));
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
