package round0.problemb.linebyline;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2024/practice-round/problems/B
 * @author atanasg
 */
public class LineByLineSolver {

	private static final Path RES_FOLDER = Paths.get("res", "round0", "problemb", "linebyline");

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
	
	private static double calculateRequiredProbabilityIncrease(int numLines, int percent) {
		// the required new percentage is: newPercent = nthRoot(percent^(numLines-1)*100)
		// which is transformed to: 100^(1/numLines) * p^((numLines - 1)/numLines)
		double numLinesRootOf100 = Math.pow(100, 1.0 / numLines);
		double secondTermWithP = Math.pow(percent, 1.0 * (numLines - 1) / numLines);
		double newPercent = numLinesRootOf100 * secondTermWithP;
		
		return newPercent - percent;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String params = buff.readLine();
			String[] paramsArray = params.split(" ");
			int n = Integer.parseInt(paramsArray[0]);
			int p = Integer.parseInt(paramsArray[1]);

			outputLines.add("Case #" + (i + 1) + ": " + calculateRequiredProbabilityIncrease(n, p));
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
