package round2.problemc1.valetparking;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2021/round-2/problems/C1
 * @author atanasg
 */
public class ValetParkingSolver {

	private static final Path RES_FOLDER = Paths.get("res", "round2", "problemc1", "valetparking");

	private static final int X = 1;
	private static final int EMPTY_CELL = 0;


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

	private static int calculateMinMoves(int[][] parking, int r, int c, int k) {
		int carsInK = 0;
		for (int i = 0; i < c; i++) {
			if(parking[k-1][i] == 1) {
				carsInK++;
			}
		}

		if (carsInK == 0) {
			return 0;
		} else if (r == 1) {
			return carsInK;
		} else {
			int minMoves = carsInK;

			int[][] parkingUp = new int[Math.max(r, k + carsInK)][c];
			for(int i = 0; i < parkingUp.length; i++) {
				for(int j = 0; j < c; j++) {
					if(i <= r-1) {
						parkingUp[i][j] = parking[i][j];
					} else {
						parkingUp[i][j] = 0;
					}
				}
			}
			int[] parkingUpRemovals = new int[parkingUp.length];
			Arrays.fill(parkingUpRemovals, 0);

			for(int i = 1; i < parkingUp.length; i++) {
				for(int j = 0; j < c; j++) {
					if(parkingUp[i][j] == 1) {
						parkingUpRemovals[i] = parkingUpRemovals[i] + 1;
						parkingUp[i][j] = parkingUp[i-1][j] + 1;
					} else {
						parkingUp[i][j] = parkingUp[i-1][j];
						if(parkingUp[i][j] >= k) {
							parkingUpRemovals[i] = parkingUpRemovals[i] + 1;
						}
					}
				}
			}

			for(int i = 1; i <= carsInK; i++) {
				int upMovesNeeded = i;
				int totalMovesNeeded = upMovesNeeded + parkingUpRemovals[k-1+i];
				if(totalMovesNeeded < minMoves) {
					minMoves = totalMovesNeeded;
				}
			}


			int[][] parkingDown = new int[r + carsInK][c];
			for(int i = 0; i < r + carsInK; i++) {
				for(int j = 0; j < c; j++) {
					if(i <= r-1) {
						parkingDown[r + carsInK - 1 - i][j] = parking[r-1-i][j];
					} else {
						parkingDown[r + carsInK - 1 - i][j] = 0;
					}
				}
			}
			int[] parkingDownRemovals = new int[r+carsInK];
			Arrays.fill(parkingDownRemovals, 0);

			for(int i = r+carsInK-2; i >= 0; i--) {
				for(int j = 0; j < c; j++) {
					if(parkingDown[i][j] == 1) {
						parkingDownRemovals[i] = parkingDownRemovals[i] + 1;
						parkingDown[i][j] = parkingDown[i+1][j] + 1;
					} else {
						parkingDown[i][j] = parkingDown[i+1][j];
						if(parkingDown[i][j] >= r-k+1) {
							parkingDownRemovals[i] = parkingDownRemovals[i] + 1;
						}
					}
				}
			}

			for(int i = 1; i <= carsInK; i++) {
				int downMovesNeeded = i;
				int totalMovesNeeded = downMovesNeeded + parkingDownRemovals[carsInK + k -1 -i];
				if(totalMovesNeeded < minMoves) {
					minMoves = totalMovesNeeded;
				}
			}

			return minMoves;
		}
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String params = buff.readLine();
			String[] paramsArray = params.split(" ");
			int r = Integer.parseInt(paramsArray[0]);
			int c = Integer.parseInt(paramsArray[1]);
			int k = Integer.parseInt(paramsArray[2]);
			int[][] parking = new int[r][c];
			
			for(int j = 0; j < r; j++) {
				String row = buff.readLine();
				// map to numbers for easier handling later
				row = row.replace('X', Character.forDigit(X, 10));
				row = row.replace('.', Character.forDigit(EMPTY_CELL, 10));
				for (int t = 0; t < c; t++) {
					parking[j][t] = Integer.parseInt(row.charAt(t) + "");
				}
			}

			int parkingSolution = calculateMinMoves(parking, r, c, k);
			outputLines.add("Case #" + (i + 1) + ": " + parkingSolution);
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
