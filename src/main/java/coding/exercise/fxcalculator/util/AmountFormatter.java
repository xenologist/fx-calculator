package coding.exercise.fxcalculator.util;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

import coding.exercise.fxcalculator.domain.ReferenceData;

/**
 * Helper class that formats amounts in a given currency.
 */
public class AmountFormatter {
    private final ReferenceData referenceData;
    private final RoundingMode roundingMode;

    public AmountFormatter(ReferenceData referenceData, RoundingMode roundingMode) {
        this.referenceData = requireNonNull(referenceData);
        this.roundingMode = requireNonNull(roundingMode);
    }

    public String format(BigDecimal amount, String currency) {
        return amount
                .setScale(referenceData.getDisplayPrecision(currency), roundingMode)
                .toPlainString();
    }
}
