package round0.problema1.consistency;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2021/qualification-round/problems/A1
 * @author atanasg
 */
public class ConsistencySolver {

	private static final Path RES_FOLDER = Paths.get("res", "round0", "problema1", "consistency");

	private static Set<String> VOWELS = new HashSet<>(Arrays.asList("A", "E", "I", "O", "U"));

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

	private static int calculateStepsToConvertInConsistentString(String birthdayString) {
		int stepsNeeded = Integer.MAX_VALUE;

		// Example birthday string: FOXENN
		// lettersOccurances = {E=1, F=1, X=1, N=2, O=1}
		System.out.println(birthdayString);
		Map<String, Long> lettersOccurances = birthdayString.codePoints().mapToObj(c -> String.valueOf((char) c))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		System.out.println(lettersOccurances);

		if (lettersOccurances.keySet().size() == 1) {
			// string contains only the same letter, so already consistent
			stepsNeeded = 0;
		} else {
			// Following above example:
			// vowelsSubmap = {E=1, O=1}
			// consonantsSubmap = {F=1, X=1, N=2}
			Map<String, Long> vowelsSubmap = VOWELS.stream().filter(lettersOccurances::containsKey)
					.collect(Collectors.toMap(Function.identity(), lettersOccurances::get));

			Map<String, Long> consonantsSubmap = lettersOccurances.keySet().stream().filter(k -> !VOWELS.contains(k))
					.collect(Collectors.toMap(Function.identity(), lettersOccurances::get));

			System.out.println(vowelsSubmap);
			System.out.println(consonantsSubmap);

			int toVowelReplacements = calculateMinStepsToConsistentString(vowelsSubmap, consonantsSubmap);
			int toConsonantReplacements = calculateMinStepsToConsistentString(consonantsSubmap, vowelsSubmap);
			stepsNeeded = Math.min(toVowelReplacements, toConsonantReplacements);
		}
		System.out.println("---");
		return stepsNeeded;
	}

	private static int calculateMinStepsToConsistentString(Map<String, Long> targetTypeLettersSubmap,
			Map<String, Long> otherTypeLettersSubmap) {
		int steps = 0;

		// all letters from the other type should be replaced by the same letter
		// of the target type,
		// e.g "BCB" -> "AAA" (if vowels are the target type)
		steps += otherTypeLettersSubmap.values().stream().mapToInt(l -> Math.toIntExact(l)).sum();

		// no additional steps in case we have 0 or 1 letters from the target type in the string
		if (targetTypeLettersSubmap.keySet().size() > 1) {
			List<Long> targetTypeValues = new ArrayList<Long>(targetTypeLettersSubmap.values());
			Collections.sort(targetTypeValues);
			// Only the letter with the largest value will remain unchanged, all others need
			// two steps of replacement, e.g. E -> X -> A:
			// 1 - replace letter to some letter from the other type
			// 2 - replace back to the "top-occurrence" letter of the target type
			for (int i = 0; i < targetTypeValues.size() - 1; i++) {
				steps += 2 * Math.toIntExact(targetTypeValues.get(i));
			}
		}
		return steps;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String birthdayString = buff.readLine();
			// 1 step == 1 second as defined in the problem description
			int timeToConvertInConsistentString = calculateStepsToConvertInConsistentString(birthdayString);
			outputLines.add("Case #" + (i + 1) + ": " + timeToConvertInConsistentString);
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
