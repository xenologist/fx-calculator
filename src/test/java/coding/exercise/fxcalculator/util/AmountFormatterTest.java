package coding.exercise.fxcalculator.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Before;
import org.junit.Test;

import coding.exercise.fxcalculator.domain.impl.SimpleReferenceData;
import coding.exercise.fxcalculator.util.AmountFormatter;

public class AmountFormatterTest {
    private AmountFormatter sut;

    @Before
    public void setUp() {
        SimpleReferenceData referenceData = new SimpleReferenceData(1);

        referenceData.addDisplayPrecision("AUD", 2);
        referenceData.addDisplayPrecision("JPY", 0);

        sut = new AmountFormatter(referenceData, RoundingMode.HALF_UP);
    }

    @Test
    public void testFormat_whenKnownCurrency_thenUseConfiguredPrecision() {
        assertEquals("1.23", sut.format(new BigDecimal("1.2345"), "AUD"));
        assertEquals("123", sut.format(new BigDecimal("123.456"), "JPY"));
    }

    @Test
    public void testFormat_whenUnknownCurrency_thenUseDefaultPrecision() {
        assertEquals("1.2", sut.format(new BigDecimal("1.2345678"), "XXX"));
    }

    @Test
    public void testFormat_whenRoundingRequired_thenApplyConfiguredRounding() {
        assertEquals("8765.43", sut.format(new BigDecimal("8765.4321"), "AUD"));
        assertEquals("1234.57", sut.format(new BigDecimal("1234.5678"), "AUD"));
    }
}
