package coding.exercise.fxcalculator.domain;

import java.util.Optional;

/**
 * Represents a rate source, i.e. a mechanism to look up an exchange rate for a
 * given currency pair.
 *
 * @param <N>
 *            underlying numeric type
 */
public interface RateSource<N> {
    Optional<N> getRate(CurrencyPair currencyPair);
}
