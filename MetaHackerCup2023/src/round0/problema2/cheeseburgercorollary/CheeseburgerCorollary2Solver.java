package round0.problema2.cheeseburgercorollary;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * @see https://www.facebook.com/codingcompetitions/hacker-cup/2023/practice-round/problems/A1
 * @author atanasg
 */
record BurgersPurcase(long countSingleBurgers, long countDoubleBurgers) {}

public class CheeseburgerCorollary2Solver {
	
	private static final Path RES_FOLDER = Paths.get("res", "round0", "problema2", "cheeseburgercorollary");
	
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
	
	private static BurgersPurcase calculateOptimalPurchase(long priceSingleBurger, long priceDoubleBurger,
			long maxBudget) {
		if (maxBudget < priceSingleBurger && maxBudget < priceDoubleBurger) {
			// nothing to buy
			return new BurgersPurcase(0L, 0L);
		} else if (priceDoubleBurger <= priceSingleBurger) {
			// no sense to buy single burger
			return new BurgersPurcase(0L, maxBudget / priceDoubleBurger);
		} else if (priceSingleBurger * 2 <= priceDoubleBurger) {
			// no sense to buy double burger
			return new BurgersPurcase(maxBudget / priceSingleBurger, 0L);
		} else {
			// priceDouble is in the range (priceSingle, 2*priceSingle)
			// This means double has more value per unit cost
			//
			// It can only make sense to buy 0, 1 or 2 single burgers
			// If you buy 3, you can also buy 1 double and 1 single (both result k = 3)
			// If you buy 4, you can also buy 1 double and 2 single (both result k = 4)
			// .. and so on
			TreeMap<Long, BurgersPurcase> possiblePurchases = new TreeMap<>();
			
			BurgersPurcase zeroSinglePurchase = new BurgersPurcase(0L, maxBudget / priceDoubleBurger);
			long zeroSinglePurchaseKDeckerHeight = calculateKDeckerHeight(zeroSinglePurchase);
			possiblePurchases.put(zeroSinglePurchaseKDeckerHeight, zeroSinglePurchase);
			
			if (maxBudget - priceSingleBurger >= 0) {
				BurgersPurcase oneSinglePurchase = new BurgersPurcase(1L, (maxBudget - priceSingleBurger) / priceDoubleBurger);
				long oneSinglePurchaseKDeckerHeight = calculateKDeckerHeight(oneSinglePurchase);
				possiblePurchases.put(oneSinglePurchaseKDeckerHeight, oneSinglePurchase);
			}
			
			if (maxBudget - 2 * priceSingleBurger >= 0) {
				BurgersPurcase twoSinglePurchase = new BurgersPurcase(2L, (maxBudget - 2 * priceSingleBurger) / priceDoubleBurger);
				long twoSinglePurchaseKDeckerHeight = calculateKDeckerHeight(twoSinglePurchase);
				possiblePurchases.put(twoSinglePurchaseKDeckerHeight, twoSinglePurchase);
			}
			
			return possiblePurchases.lastEntry().getValue();
		}
	}

	private static long calculateKDeckerHeight(BurgersPurcase optimalPurchase) {
		if (optimalPurchase.countSingleBurgers() == 0L && optimalPurchase.countDoubleBurgers() == 0L) {
			// no burgers
			return 0L;
		} else if (optimalPurchase.countDoubleBurgers() == 0L) {
			// every single burger is one deck
			return optimalPurchase.countSingleBurgers();
		} else if (optimalPurchase.countSingleBurgers() == 0L) {
			// every double burger is 2 decks but the last burger can not be fully used
			return optimalPurchase.countDoubleBurgers() * 2 - 1 ;
		} else {
			return optimalPurchase.countDoubleBurgers() * 2 + optimalPurchase.countSingleBurgers();
		}
	}

	private static long buildHighestKdeckerBurger(long priceSingleBurger, long priceDoubleBurger, long maxBudget) {
		BurgersPurcase optimalPurchase = calculateOptimalPurchase(priceSingleBurger, priceDoubleBurger, maxBudget);
		return calculateKDeckerHeight(optimalPurchase);
	}

	public static void main(String[] args) throws IOException {

		BufferedReader buff = Files.newBufferedReader(RES_FOLDER.resolve("input.txt"));

		initStaticVariables(Integer.parseInt(buff.readLine()));

		for (int i = 0; i < numOfCases; i++) {
			String testCaseInput = buff.readLine();
			String[] testCaseArray = testCaseInput.split(WHITESPACE);
			long a = Long.parseLong(testCaseArray[0]);
			long b = Long.parseLong(testCaseArray[1]);
			long c = Long.parseLong(testCaseArray[2]);
			
			long k = buildHighestKdeckerBurger(a, b, c);
			outputLines.add("Case #" + (i + 1) + ": " + k);
		}

		buff.close();

		writeOutputToFile(RES_FOLDER.resolve("output.txt"));
	}
}
