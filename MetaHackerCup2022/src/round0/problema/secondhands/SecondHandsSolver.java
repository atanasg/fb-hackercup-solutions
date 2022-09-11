package round0.problema.secondhands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2022/qualification-round/problems/A
 * @author atanasg
 */
public class SecondHandsSolver {
	
	private static final Path RES_FOLDER = Paths.get("res", "round0", "problema", "secondhands");
	
	private static final String WHITESPACE = " ";
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
	
	private static String determineIfPartsFitInShowcases(int n, int k, List<Integer> partsList) {
		boolean isSolutionPossible = true;
		
		if (n > (2 * k)) {
			// too many parts for the two displays
			isSolutionPossible = false;
		}
		
		int[] styles = new int[101];
		Arrays.fill(styles, 0);
		
		for(int p : partsList) {
			styles[p] = styles[p] + 1;
			if (styles[p] >= 3) {
				// a same style will be twice in one of the displays
				isSolutionPossible = false;
				break;
			}
		}
		
		if (isSolutionPossible) {
			return TRUE_CASE;
		} else {
			return FALSE_CASE;
		}
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String nk = buff.readLine();
			String[] nkArray = nk.split(WHITESPACE);
			int n = Integer.parseInt(nkArray[0]);
			int k = Integer.parseInt(nkArray[1]);
			
			String parts = buff.readLine();
			String[] partsArray = parts.split(WHITESPACE);
			
			List<Integer> partsList = new LinkedList<>();
			for (String p : partsArray) {
				partsList.add(Integer.parseInt(p));
			}
			
			//Arrays.stream(partsArray).map(p -> Integer.parseInt(p)).collect(Collectors.toList());
			
			String yesOrNo = determineIfPartsFitInShowcases(n, k, partsList);
			outputLines.add("Case #" + (i + 1) + ": " + yesOrNo);
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
