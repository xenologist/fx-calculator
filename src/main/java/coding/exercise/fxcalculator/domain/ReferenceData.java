package coding.exercise.fxcalculator.domain;

import java.util.Optional;

/**
 * Provides necessary information about currencies and currency pairs.
 */
public interface ReferenceData {
    /**
     * If either a direct or a inverted feed exists for this currency pair, returns
     * the currency pair of the feed.
     * 
     * For example, if a feed for {@code AUDUSD} exists, then for both
     * {@code AUDUSD} and {@code USDAUD} this method will return {@code AUDUSD}.
     * 
     * @param currencyPair
     * @return quoted currency pair or empty if cross
     */
    Optional<CurrencyPair> getFeedCurrencyPair(CurrencyPair currencyPair);

    /**
     * If the given currency pair is a cross, returns the split currency for this
     * cross.
     * 
     * @param currencyPair
     *            currency pair
     * @return split currency or empty if not a cross
     */
    Optional<String> getSplitCurrency(CurrencyPair currencyPair);

    /**
     * Returns the number of decimal points that should be used when displaying
     * amounts in the given currency.
     * 
     * @param currency
     * @return number of decimal points to display
     */
    int getDisplayPrecision(String currency);
}
