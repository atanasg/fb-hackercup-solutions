package round0.problemb.xsandos;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

class SolutionData {
	private boolean solutionPossible;
	private int minRequiredX;
	private int numOfWinningSets;
	
	SolutionData() {
		this.solutionPossible = false;
		this.minRequiredX = Integer.MAX_VALUE;
		this.numOfWinningSets = 0;
	}

	public boolean isSolutionPossible() {
		return solutionPossible;
	}

	public void setSolutionPossible(boolean solutionPossible) {
		this.solutionPossible = solutionPossible;
	}

	public int getMinRequiredX() {
		return minRequiredX;
	}

	public void setMinRequiredX(int minRequiredX) {
		this.minRequiredX = minRequiredX;
	}

	public int getNumOfWinningSets() {
		return numOfWinningSets;
	}

	public void setNumOfWinningSets(int numOfWinningSets) {
		this.numOfWinningSets = numOfWinningSets;
	}
}

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2021/qualification-round/problems/B
 * @author atanasg
 */
public class XsAndOsSolver {

	private static final Path RES_FOLDER = Paths.get("res", "round0", "problemb", "xsandos");

	private static final int X = 1;
	private static final int O = 2;
	private static final int EMPTY_CELL = 0;

	private static final String NO_SOLUTION = "Impossible";

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

	private static String calculateXWinPossibilities(int sizeN, int[][] gameMatrix) {
		SolutionData solutionData = new SolutionData();
		
		for(int i = 0; i < sizeN; i++) {
			// check row i
			int numEmpty = rowCountSymbol(gameMatrix, i, EMPTY_CELL);
			if (numEmpty > 0 && rowCountSymbol(gameMatrix, i, O) == 0) {
				updatePossibleSolution(solutionData, numEmpty);
			}

			// check column i
			numEmpty = columnCountSymbol(gameMatrix, i, EMPTY_CELL);
			if (numEmpty > 0 && columnCountSymbol(gameMatrix, i, O) == 0) {
				updatePossibleSolution(solutionData, numEmpty);
			}
		}
		
		if (solutionData.isSolutionPossible()) {
			if (solutionData.getMinRequiredX() == 1 && solutionData.getNumOfWinningSets() > 1) {
				// Clean up possible duplicates where a single X completes a full row and a full
				// column. See example:
				//  OXO
				//  X.X
				//  OXO
				// The answer here should be "1 1" and not "1 2";
				int numSingleWinningCrossings = countSingleWinningCrossings(gameMatrix, sizeN);
				// "crossings" were counted above twice (for the row and for the column),
				// so here we remove the counted duplications
				solutionData.setNumOfWinningSets(solutionData.getNumOfWinningSets() - numSingleWinningCrossings);
			}
			return solutionData.getMinRequiredX() + " " + solutionData.getNumOfWinningSets();
		} else {
			return NO_SOLUTION;
		}
	}

	private static void updatePossibleSolution(SolutionData solData, int numEmpty) {
		solData.setSolutionPossible(true);

		if (numEmpty < solData.getMinRequiredX()) {
			solData.setMinRequiredX(numEmpty);
			solData.setNumOfWinningSets(1);
		} else if (numEmpty == solData.getMinRequiredX()) {
			solData.setNumOfWinningSets(solData.getNumOfWinningSets() + 1);
		}
	}

	/**
	 * Detect and count cases like:
	 * 
	 * OXO
	 * X.X
	 * OXO
	 * 
	 * @param gameMatrix
	 * @param sizeN
	 * @return
	 */
	private static int countSingleWinningCrossings(int[][] gameMatrix, int sizeN) {
		int crossings = 0;

		for (int i = 0; i < sizeN; i++) {
			for (int j = 0; j < sizeN; j++) {
				if (gameMatrix[i][j] == EMPTY_CELL &&
						rowCountSymbol(gameMatrix, i, X) == (sizeN - 1) &&
						columnCountSymbol(gameMatrix, j, X) == (sizeN - 1)) {
					crossings++;
				}
			}
		}
		return crossings;
	}

	private static int rowCountSymbol(int[][] gameMatrix, int rowIndex, int symbol) {
		return countSymbol(gameMatrix, rowIndex, true, symbol);
	}

	private static int columnCountSymbol(int[][] gameMatrix, int colIndex, int symbol) {
		return countSymbol(gameMatrix, colIndex, false, symbol);
	}

	private static int countSymbol(int[][] gameMatrix, int index, boolean row, int symbol) {
		int count = 0;
		
		for (int j = 0; j < gameMatrix.length; j++) {
			if(row && gameMatrix[index][j] == symbol) {
				// count in a row
				count++;
			} else if (!row && gameMatrix[j][index] == symbol) {
				// count in a column
				count++;
			}
		}
		return count;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			int sizeN = Integer.parseInt(buff.readLine());
			int[][] gameMatrix = new int[sizeN][sizeN];
			
			for(int j = 0; j < sizeN; j++) {
				String row = buff.readLine();
				// map to numbers for easier handling later
				row = row.replace('X', Character.forDigit(X, 10));
				row = row.replace('O', Character.forDigit(O, 10));
				row = row.replace('.', Character.forDigit(EMPTY_CELL, 10));
				for (int k = 0; k < sizeN; k++) {
					gameMatrix[j][k] = Integer.parseInt(row.charAt(k) + "");
				}
			}

			String gameSolution = calculateXWinPossibilities(sizeN, gameMatrix);
			outputLines.add("Case #" + (i + 1) + ": " + gameSolution);
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
