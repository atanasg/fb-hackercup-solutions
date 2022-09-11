package round0.problemb1.secondfriend;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2022/qualification-round/problems/B1
 * @author atanasg
 */
public class SecondFriendSolver {
	
	private static final Path RES_FOLDER = Paths.get("res", "round0", "problemb1", "secondfriend");
	
	private static final String TREE = "^";
	private static final String TRUE_CASE = "Possible";
	private static final String FALSE_CASE = "Impossible";

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
	
	private static boolean noTreeOnPainting(String[][] painting) {
		boolean noTree = true;
		outer:
		for (String[] row : painting) {
			for(String cell : row) {
				if (cell.equals(TREE)) {
					noTree = false;
					break outer;
				}
			}
		}
		return noTree;
	}
	
	private static List<String> checkOrMakeNoLonelyTrees(int r, int c, String[][] painting) {
		List<String> res = new LinkedList<>();
		
		if (noTreeOnPainting(painting)) {
			res.add(TRUE_CASE);
			for (String[] row : painting) {
				res.add(String.join("", row));
			}
		} else if (r == 1 || c == 1) {
			res.add(FALSE_CASE);
		} else {
			// make all fields trees
			res.add(TRUE_CASE);
			for (int i = 0; i < r; i++) {
				res.add(TREE.repeat(c));
			}
		}
		return res;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String rc = buff.readLine();
			String[] rcArray = rc.split(WHITESPACE);
			int r = Integer.parseInt(rcArray[0]);
			int c = Integer.parseInt(rcArray[1]);
			
			String[][] painting = new String[r][c];
			
			for (int k = 0; k < r; k++) {
				String line = buff.readLine();
				String[] lineArray = line.split("");
				painting[k] = lineArray;
			}
			
			List<String> impossibleOrSolution = checkOrMakeNoLonelyTrees(r, c, painting);
			outputLines.add("Case #" + (i + 1) + ": " + impossibleOrSolution.get(0));
			for (int k = 1 ; k < impossibleOrSolution.size(); k++) {
				outputLines.add(impossibleOrSolution.get(k));
			}
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
