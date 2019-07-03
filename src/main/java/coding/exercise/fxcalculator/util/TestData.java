package coding.exercise.fxcalculator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

import coding.exercise.fxcalculator.domain.CurrencyPair;
import coding.exercise.fxcalculator.domain.RateSource;
import coding.exercise.fxcalculator.domain.ReferenceData;
import coding.exercise.fxcalculator.domain.impl.SimpleReferenceData;

/**
 * Provides canned reference data and rates from packaged resource files.
 */
public class TestData {
    public static final int DEFAULT_DISPLAY_PRECISION = 2;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private static final String RATES_CSV = "rates.csv";
    private static final String CROSSES_CSV = "crosses.csv";
    private static final String DISPLAY_PRECISION_CSV = "displayprecision.csv";

    private static final Pattern CSV_DELIMITER = Pattern.compile("\\s|,");

    private final SimpleReferenceData referenceData;
    private final Map<CurrencyPair, Optional<BigDecimal>> rates;

    public TestData() {
        referenceData = new SimpleReferenceData(DEFAULT_DISPLAY_PRECISION);
        rates = new LinkedHashMap<>();

        loadMarketData(RATES_CSV);
        loadCrosses(CROSSES_CSV);
        loadDisplayPrecision(DISPLAY_PRECISION_CSV);
    }

    public ReferenceData getReferenceData() {
        return referenceData;
    }

    public RateSource<BigDecimal> getRateSource() {
        return currencyPair -> rates.getOrDefault(currencyPair, Optional.empty());
    }

    private void loadMarketData(String resourceName) {
        try (Scanner scanner = openResource(resourceName)) {
            scanner.useDelimiter(CSV_DELIMITER);
            scanner.nextLine(); // Skip header

            while (scanner.hasNext()) {
                String baseCurrency = readCurrency(scanner);
                String termsCurrency = readCurrency(scanner);
                BigDecimal rate = readRate(scanner);

                CurrencyPair currencyPair = new CurrencyPair(baseCurrency, termsCurrency);

                rates.put(currencyPair, Optional.of(rate));
                referenceData.addFeedCurrencyPair(currencyPair);
            }
        }
    }

    private void loadCrosses(String resourceName) {
        try (Scanner scanner = openResource(resourceName)) {
            scanner.useDelimiter(CSV_DELIMITER);
            scanner.nextLine(); // Skip header

            while (scanner.hasNext()) {
                String baseCurrency = readCurrency(scanner);
                String termsCurrency = readCurrency(scanner);
                String splitCurrency = readCurrency(scanner);

                referenceData.addSplitCurrency(new CurrencyPair(baseCurrency, termsCurrency), splitCurrency);
            }
        }
    }

    private void loadDisplayPrecision(String resourceName) {
        try (Scanner scanner = openResource(resourceName)) {
            scanner.useDelimiter(CSV_DELIMITER);
            scanner.nextLine(); // Skip header

            while (scanner.hasNext()) {
                String currency = readCurrency(scanner);
                int displayPrecision = readPrecision(scanner);

                referenceData.addDisplayPrecision(currency, displayPrecision);
            }
        }
    }

    private Scanner openResource(String resourceName) {
        return new Scanner(getClass().getClassLoader().getResourceAsStream(resourceName));
    }

    private String readCurrency(Scanner scanner) {
        return scanner.next().toUpperCase();
    }

    private BigDecimal readRate(Scanner scanner) {
        return scanner.nextBigDecimal();
    }

    private int readPrecision(Scanner scanner) {
        return scanner.nextInt();
    }
}
