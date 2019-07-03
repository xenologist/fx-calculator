package coding.exercise.fxcalculator.domain.impl;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

import coding.exercise.fxcalculator.domain.Arithmetic;

/**
 * Implements an {@link Arithmetic} based on BigDecimal, with configurable
 * precision and rounding.
 */
public class BigDecimalArithmetic implements Arithmetic<BigDecimal> {
    private static final BigDecimal ONE = new BigDecimal(1);

    private final int internalPrecision;
    private final RoundingMode roundingMode;

    public BigDecimalArithmetic(int internalPrecision, RoundingMode roundingMode) {
        this.internalPrecision = internalPrecision;
        this.roundingMode = requireNonNull(roundingMode);
    }

    @Override
    public BigDecimal multiply(BigDecimal first, BigDecimal second) {
        return first.multiply(second)
                .setScale(internalPrecision, roundingMode)
                .stripTrailingZeros();
    }

    @Override
    public BigDecimal invert(BigDecimal value) {
        return ONE.divide(value, internalPrecision, roundingMode)
                .stripTrailingZeros();
    }
}
