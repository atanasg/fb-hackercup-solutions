package round0.problema.warmup;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2025/practice-round/problems/A
 * @author atanasg
 */

record Dish (int index, int startTemp, int targetTemp) {}
record WarmupPair (int warmDishIndex, int coldDishIndex) {}
record WarmupResponse (int result, List<WarmupPair> warmupPairs) {}

public class WarmupSolver {

	private static final Path RES_FOLDER = Paths.get("res", "round0", "problema", "warmup");

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
	
	private static WarmupResponse calculateWarmupSequence(int n, int[] startTemps, int[] targetTemps) {
		Map<Integer, Integer> temperatureProviderMap = new HashMap<>();
		for (int i = 0; i < n; i++) {
			temperatureProviderMap.put(startTemps[i], i);
		}
		
		// case 0: target state initially reached
		boolean isTargetStateReached = true;
		for (int i = 0; i < n; i++) {
			if(startTemps[i] != targetTemps[i]) {
				isTargetStateReached = false;
				break;
			}
		}
		if (isTargetStateReached) {
			return new WarmupResponse(0, null);
		}
		
		// case -1: impossible cooldown needed
		boolean isCooldownNeeded = false;
		for (int i = 0; i < n; i++) {
			if(startTemps[i] > targetTemps[i]) {
				isCooldownNeeded = true;
				break;
			}
		}
		if (isCooldownNeeded) {
			return new WarmupResponse(-1, null);
		}
		
		// case -1: no desired target temp
		boolean isTargetTempMissing = false;
		for (int i = 0; i < n; i++) {
			if(temperatureProviderMap.get(targetTemps[i]) == null) {
				isTargetTempMissing = true;
				break;
			}
		}
		if (isTargetTempMissing) {
			return new WarmupResponse(-1, null);
		}
		
		// case x: the regular case
		return calculateWarmupSequenceRegularCase(n, startTemps, targetTemps, temperatureProviderMap);
	}
	
	private static WarmupResponse calculateWarmupSequenceRegularCase(int n, int[] startTemps, int[] targetTemps,
			Map<Integer, Integer> temperatureProviderMap) {
		int warmupCount = 0;
		List<WarmupPair> warmupPairs = new LinkedList<>();
		
		List<Dish> dishesForWarmup = new LinkedList<>();
		for (int i = 0; i < n; i++) {
			if (startTemps[i] < targetTemps[i]) {
				dishesForWarmup.add(new Dish(i, startTemps[i], targetTemps[i]));
			}
		}
		
		// sort according to start temp, so that no overrides of temperatures can happen if a low-temp dish
		// is needed at some place but was already warmed up and does not exist.
		dishesForWarmup.sort(Comparator.comparing(Dish::startTemp));
		
		for(Dish d : dishesForWarmup) {
			warmupCount++;
			
			int warmDishIndex = temperatureProviderMap.get(d.targetTemp()) + 1;
			warmupPairs.add(new WarmupPair(warmDishIndex, d.index() + 1));
		}

		return new WarmupResponse(warmupCount, warmupPairs);
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			int n = Integer.parseInt(buff.readLine());
			
			String startTempsString = buff.readLine();
			String[] startTempsArray = startTempsString.split(" ");
			
			String targetTempsString = buff.readLine();
			String[] targetTempsArray = targetTempsString.split(" ");
			
			int[] startTemps = new int[n];
			int[] targetTemps = new int[n];
			
			for(int j = 0; j < n; j++) {
				startTemps[j] = Integer.parseInt(startTempsArray[j]);
				targetTemps[j] = Integer.parseInt(targetTempsArray[j]);
			}

			WarmupResponse warmupResp = calculateWarmupSequence(n, startTemps, targetTemps);
			outputLines.add("Case #" + (i + 1) + ": " + warmupResp.result());
			if (warmupResp.warmupPairs() != null) {
				for (WarmupPair wp : warmupResp.warmupPairs()) {
					outputLines.add(wp.warmDishIndex() + " " + wp.coldDishIndex());
				}
			}
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
