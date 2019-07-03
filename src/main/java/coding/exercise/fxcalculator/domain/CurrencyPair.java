package coding.exercise.fxcalculator.domain;

import static java.util.Objects.requireNonNull;

/**
 * Represents a currency pair.
 */
public class CurrencyPair {
    private final String baseCurrency;
    private final String termsCurrency;

    public CurrencyPair(String baseCurrency, String termsCurrency) {
        this.baseCurrency = requireNonNull(baseCurrency);
        this.termsCurrency = requireNonNull(termsCurrency);
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTermsCurrency() {
        return termsCurrency;
    }

    public String getCode() {
        return baseCurrency + termsCurrency;
    }

    public CurrencyPair invert() {
        return new CurrencyPair(termsCurrency, baseCurrency);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof CurrencyPair
                && baseCurrency.equals(((CurrencyPair) obj).getBaseCurrency())
                && termsCurrency.equals(((CurrencyPair) obj).getTermsCurrency()));
    }

    @Override
    public int hashCode() {
        return baseCurrency.hashCode() * 31 + termsCurrency.hashCode();
    }

    @Override
    public String toString() {
        return getCode();
    }
}
