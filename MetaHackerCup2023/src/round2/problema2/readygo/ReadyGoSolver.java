package round2.problema2.readygo;

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
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2023/round-2/problems/A2
 * @author atanasg
 */

record Cell(int yPos, int xPos) {

	@Override
	public int hashCode() {
		return Objects.hash(xPos, yPos);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		return xPos == other.xPos && yPos == other.yPos;
	}
}

public class ReadyGoSolver {

	private static final Path RES_FOLDER = Paths.get("res", "round2", "problema2", "readygo");

	private static final int B_CELL = 1;
	private static final int W_CELL = 2;
	private static final int EMPTY_CELL = 0;
	private static final Set<Cell> UNVISITED_WHITE_STONES = new HashSet<>();


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
	
	private static void putAllWhiteStonesToUnvisited(int[][] board, int r, int c) {
		for(int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				if(board[i][j] == W_CELL) {
					UNVISITED_WHITE_STONES.add(new Cell(i, j));
				}
			}
		}
	}
	
	private static void handleNeighbourCell(int cellType, int yPos, int xPos,
			Queue<Cell> dfsQueue, Set<Cell> visitedStonesForGroup, Set<Cell> emptySpacesAroundWhiteStoneGroup) {
		switch (cellType) {
		case B_CELL:
			break;
		case EMPTY_CELL:
			emptySpacesAroundWhiteStoneGroup.add(new Cell(yPos, xPos));
			break;
		case W_CELL:
			Cell whiteStone = new Cell(yPos, xPos);
			if (!visitedStonesForGroup.contains(whiteStone) && !dfsQueue.contains(whiteStone)
					&& UNVISITED_WHITE_STONES.contains(whiteStone)) {
				UNVISITED_WHITE_STONES.remove(whiteStone);
				dfsQueue.add(whiteStone);
			}
			break;
		default:
			throw new IllegalStateException("Unknown cell type");
		}
	}
	
	private static int calculateMaxCapturableWhiteGroupSize(int[][] board, int r, int c) {
		// prepare
		putAllWhiteStonesToUnvisited(board, r, c);
		
		if (UNVISITED_WHITE_STONES.isEmpty()) {
			// no white stones on the board
			return 0;
		}
		
		Map<Cell, Integer> maxCapturableWhiteGroupSizes = new HashMap<>();
		
		// identify groups
		while (UNVISITED_WHITE_STONES.size() > 0) {
			Set<Cell> emptySpacesAroundWhiteStoneGroup = new HashSet<>();
			
			// get a white stone and build its group
			Cell someUnvisitedWhiteStone = UNVISITED_WHITE_STONES.iterator().next();
			UNVISITED_WHITE_STONES.remove(someUnvisitedWhiteStone);
			
			Queue<Cell> dfsQueue = new LinkedList<>();
			dfsQueue.add(someUnvisitedWhiteStone);
			
			Set<Cell> visitedStonesForGroup = new HashSet<>();
			
			while(dfsQueue.size() > 0) {
				Cell currentWhiteStone = dfsQueue.remove();
				visitedStonesForGroup.add(currentWhiteStone);
				
				int y = currentWhiteStone.yPos();
				int x = currentWhiteStone.xPos();
				
				// left neighbour
				int leftX = x - 1;
				if (leftX >= 0) {
					handleNeighbourCell(board[y][leftX], y, leftX, dfsQueue, visitedStonesForGroup, emptySpacesAroundWhiteStoneGroup);
				}
				
				// right neighbour
				int rightX = x + 1;
				if (rightX < c) {
					handleNeighbourCell(board[y][rightX], y, rightX, dfsQueue, visitedStonesForGroup, emptySpacesAroundWhiteStoneGroup);
				}
				
				// down neighbour
				int downY = y + 1;
				if (downY < r) {
					handleNeighbourCell(board[downY][x], downY, x, dfsQueue, visitedStonesForGroup, emptySpacesAroundWhiteStoneGroup);
				}
				
				// up neighbour
				int upY = y - 1;
				if (upY >= 0) {
					handleNeighbourCell(board[upY][x], upY, x, dfsQueue, visitedStonesForGroup, emptySpacesAroundWhiteStoneGroup);
				}
			}
			
			if (emptySpacesAroundWhiteStoneGroup.size() == 1) {
				// the group is capturable if putting a black stone at the empty space
				maxCapturableWhiteGroupSizes.merge(emptySpacesAroundWhiteStoneGroup.iterator().next(), visitedStonesForGroup.size(), Integer::sum);
			}
		}
		return maxCapturableWhiteGroupSizes.values().stream().mapToInt(i -> i).max().orElse(0);
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String params = buff.readLine();
			String[] paramsArray = params.split(" ");
			int r = Integer.parseInt(paramsArray[0]);
			int c = Integer.parseInt(paramsArray[1]);
			int[][] board = new int[r][c];
			
			for(int j = 0; j < r; j++) {
				String row = buff.readLine();
				// map to numbers for easier handling later
				row = row.replace('B', Character.forDigit(B_CELL, 10));
				row = row.replace('W', Character.forDigit(W_CELL, 10));
				row = row.replace('.', Character.forDigit(EMPTY_CELL, 10));
				for (int t = 0; t < c; t++) {
					board[j][t] = Integer.parseInt(row.charAt(t) + "");
				}
			}

			int maxCapturableGroupSize = calculateMaxCapturableWhiteGroupSize(board, r, c);
			outputLines.add("Case #" + (i + 1) + ": " + maxCapturableGroupSize);
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
