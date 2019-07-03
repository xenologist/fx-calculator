package coding.exercise.fxcalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

import coding.exercise.fxcalculator.domain.Arithmetic;
import coding.exercise.fxcalculator.domain.RateSource;
import coding.exercise.fxcalculator.domain.ReferenceData;
import coding.exercise.fxcalculator.domain.impl.BigDecimalArithmetic;
import coding.exercise.fxcalculator.util.AmountFormatter;
import coding.exercise.fxcalculator.util.Terminal;
import coding.exercise.fxcalculator.util.TestData;

public class Main implements Runnable {
    private static final int INTERNAL_PRECISION = 8;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private static final Pattern IN = Pattern.compile("in", Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) {
        Terminal terminal = new Terminal(System.in, System.out);
        TestData testData = new TestData();

        new Main(terminal, testData.getReferenceData(), testData.getRateSource())
                .run();
    }

    private final Terminal terminal;
    private final CurrencyConverter<BigDecimal> currencyConverter;
    private final AmountFormatter amountFormatter;

    public Main(Terminal terminal, ReferenceData referenceData, RateSource<BigDecimal> rateSource) {
        this.terminal = terminal;

        Arithmetic<BigDecimal> arithmetic = new BigDecimalArithmetic(INTERNAL_PRECISION, ROUNDING_MODE);
        RateCalculator<BigDecimal> rateCalculator = new RateCalculator<>(referenceData, rateSource, arithmetic);

        currencyConverter = new CurrencyConverter<>(rateCalculator, arithmetic);
        amountFormatter = new AmountFormatter(referenceData, ROUNDING_MODE);
    }

    @Override
    public void run() {
        do {
            terminal.prompt("%> ");
        } while (terminal.parse(this::parseInput));
    }

    private void parseInput(Scanner scanner) {
        String fromCurrency = readCurrency(scanner);
        BigDecimal amount = readAmount(scanner);
        scanner.next(IN);
        String toCurrency = readCurrency(scanner);

        Optional<BigDecimal> result = currencyConverter.convert(amount, fromCurrency, toCurrency);

        if (result.isPresent()) {
            terminal.print(fromCurrency + " " + amountFormatter.format(amount, fromCurrency)
                    + " = " + toCurrency + " " + amountFormatter.format(result.get(), toCurrency));
        } else {
            terminal.print("Unable to find rate for " + fromCurrency + "/" + toCurrency);
        }
    }

    private String readCurrency(Scanner scanner) {
        return scanner.next().toUpperCase();
    }

    private BigDecimal readAmount(Scanner scanner) {
        return scanner.nextBigDecimal();
    }
}
