package coding.exercise.fxcalculator;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import coding.exercise.fxcalculator.domain.Arithmetic;
import coding.exercise.fxcalculator.domain.CurrencyPair;
import coding.exercise.fxcalculator.domain.RateSource;
import coding.exercise.fxcalculator.domain.ReferenceData;

/**
 * Decorates a rate source to provide missing rates by cross-calculating
 * available rates.
 *
 * @param <N>
 *            underlying numeric type
 */
public class RateCalculator<N> implements RateSource<N> {
    private final ReferenceData referenceData;
    private final RateSource<N> rateSource;
    private final Arithmetic<N> arithmetic;

    public RateCalculator(ReferenceData referenceData, RateSource<N> rateSource, Arithmetic<N> arithmetic) {
        this.referenceData = requireNonNull(referenceData);
        this.rateSource = requireNonNull(rateSource);
        this.arithmetic = requireNonNull(arithmetic);
    }

    @Override
    public Optional<N> getRate(CurrencyPair currencyPair) {
        Optional<N> feedRate = getFeedRate(requireNonNull(currencyPair));

        return feedRate.isPresent() ? feedRate : getCrossRate(currencyPair);
    }

    private Optional<N> getFeedRate(CurrencyPair currencyPair) {
        return referenceData.getFeedCurrencyPair(currencyPair)
                .flatMap(feedCurrencyPair -> rateSource.getRate(feedCurrencyPair)
                        .map(feedRate -> convertToDirect(feedRate, feedCurrencyPair, currencyPair)));
    }

    private N convertToDirect(N feedRate, CurrencyPair feedCurrencyPair, CurrencyPair currencyPair) {
        return feedCurrencyPair.equals(currencyPair) ? feedRate : arithmetic.invert(feedRate);
    }

    private Optional<N> getCrossRate(CurrencyPair currencyPair) {
        return referenceData.getSplitCurrency(currencyPair)
                .flatMap(splitCurrency -> getRate(getFirstSplit(currencyPair, splitCurrency))
                        .flatMap(firstRate -> getRate(getSecondSplit(currencyPair, splitCurrency))
                                .map(secondRate -> arithmetic.multiply(firstRate, secondRate))));
    }

    private CurrencyPair getFirstSplit(CurrencyPair currencyPair, String splitCurrency) {
        return new CurrencyPair(currencyPair.getBaseCurrency(), splitCurrency);
    }

    private CurrencyPair getSecondSplit(CurrencyPair currencyPair, String splitCurrency) {
        return new CurrencyPair(splitCurrency, currencyPair.getTermsCurrency());
    }
}
