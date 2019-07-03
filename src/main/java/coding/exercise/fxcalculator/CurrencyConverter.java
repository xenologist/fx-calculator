package coding.exercise.fxcalculator;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import coding.exercise.fxcalculator.domain.Arithmetic;
import coding.exercise.fxcalculator.domain.CurrencyPair;
import coding.exercise.fxcalculator.domain.RateSource;

/**
 * Converts amounts between currencies based on the given rate source.
 * 
 * @param <N>
 *            underlying numeric type
 */
public class CurrencyConverter<N> {
    private final RateSource<N> rateSource;
    private final Arithmetic<N> arithmetic;

    public CurrencyConverter(RateSource<N> rateSource, Arithmetic<N> arithmetic) {
        this.rateSource = requireNonNull(rateSource);
        this.arithmetic = requireNonNull(arithmetic);
    }

    public Optional<N> convert(N amount, String fromCurrency, String toCurrency) {
        requireNonNull(amount);
        requireNonNull(fromCurrency);
        requireNonNull(toCurrency);

        return fromCurrency.equalsIgnoreCase(toCurrency)
                ? Optional.of(amount)
                : convert(amount, new CurrencyPair(fromCurrency, toCurrency));
    }

    private Optional<N> convert(N amount, CurrencyPair currencyPair) {
        return rateSource.getRate(currencyPair)
                .map(rate -> arithmetic.multiply(amount, rate));
    }
}
