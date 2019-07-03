package coding.exercise.fxcalculator;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import coding.exercise.fxcalculator.CurrencyConverter;
import coding.exercise.fxcalculator.domain.Arithmetic;
import coding.exercise.fxcalculator.domain.CurrencyPair;
import coding.exercise.fxcalculator.domain.impl.BigDecimalArithmetic;

public class CurrencyConverterTest {
    private static final String AUD = "AUD";
    private static final String USD = "USD";

    private static final CurrencyPair AUDUSD = new CurrencyPair(AUD, USD);

    private CurrencyConverter<BigDecimal> sut;

    @Before
    public void setUp() {
        Arithmetic<BigDecimal> arithmetic = new BigDecimalArithmetic(4, RoundingMode.HALF_UP);
        Map<CurrencyPair, Optional<BigDecimal>> rates = new LinkedHashMap<>();

        rates.put(AUDUSD, Optional.of(new BigDecimal("0.7")));

        sut = new CurrencyConverter<>(
                currencyPair -> rates.getOrDefault(currencyPair, Optional.empty()),
                arithmetic);
    }

    @Test
    public void testConvert_whenRateAvailable_thenReturnConvertedAmount() {
        assertEquals(Optional.of(new BigDecimal("8.638")), sut.convert(new BigDecimal("12.34"), AUD, USD));
    }

    @Test
    public void testConvert_whenRateNotAvailable_thenReturnEmpty() {
        assertEquals(Optional.empty(), sut.convert(new BigDecimal("12.34"), USD, AUD));
    }
}
