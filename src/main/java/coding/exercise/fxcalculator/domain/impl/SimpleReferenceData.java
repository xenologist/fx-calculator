package coding.exercise.fxcalculator.domain.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import coding.exercise.fxcalculator.domain.CurrencyPair;
import coding.exercise.fxcalculator.domain.ReferenceData;

/**
 * Simple implementation of {@link ReferenceData} that can be populated by
 * directly adding values to it.
 */
public class SimpleReferenceData implements ReferenceData {
    private final Map<CurrencyPair, Optional<CurrencyPair>> feedCurrencyPairs = new LinkedHashMap<>();
    private final Map<CurrencyPair, Optional<String>> splitCurrencies = new LinkedHashMap<>();
    private final Map<String, Integer> displayPrecisions = new LinkedHashMap<>();

    private final Integer defaultDisplayPrecision;

    public SimpleReferenceData(int defaultDisplayPrecision) {
        this.defaultDisplayPrecision = defaultDisplayPrecision;
    }

    public void addFeedCurrencyPair(CurrencyPair currencyPair) {
        Optional<CurrencyPair> value = Optional.of(currencyPair);

        feedCurrencyPairs.put(currencyPair, value);
        feedCurrencyPairs.put(currencyPair.invert(), value);
    }

    public void addSplitCurrency(CurrencyPair currencyPair, String splitCurrency) {
        Optional<String> value = Optional.of(splitCurrency);

        splitCurrencies.put(currencyPair, value);
        splitCurrencies.put(currencyPair.invert(), value);
    }

    public void addDisplayPrecision(String currency, int precision) {
        displayPrecisions.put(currency, precision);
    }

    @Override
    public Optional<CurrencyPair> getFeedCurrencyPair(CurrencyPair currencyPair) {
        return feedCurrencyPairs.getOrDefault(currencyPair, Optional.empty());
    }

    @Override
    public Optional<String> getSplitCurrency(CurrencyPair currencyPair) {
        return splitCurrencies.getOrDefault(currencyPair, Optional.empty());
    }

    @Override
    public int getDisplayPrecision(String currency) {
        return displayPrecisions.getOrDefault(currency, defaultDisplayPrecision);
    }
}
