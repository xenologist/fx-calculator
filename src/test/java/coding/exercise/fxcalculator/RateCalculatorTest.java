package coding.exercise.fxcalculator;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import coding.exercise.fxcalculator.RateCalculator;
import coding.exercise.fxcalculator.domain.Arithmetic;
import coding.exercise.fxcalculator.domain.CurrencyPair;
import coding.exercise.fxcalculator.domain.impl.BigDecimalArithmetic;
import coding.exercise.fxcalculator.domain.impl.SimpleReferenceData;

public class RateCalculatorTest {
    private static final String AUD = "AUD";
    private static final String EUR = "EUR";
    private static final String JPY = "JPY";
    private static final String NOK = "NOK";
    private static final String USD = "USD";
    private static final String OMG = "OMG";
    private static final String WTF = "WTF";

    private static final CurrencyPair AUDEUR = new CurrencyPair(AUD, EUR);
    private static final CurrencyPair AUDNOK = new CurrencyPair(AUD, NOK);
    private static final CurrencyPair AUDJPY = new CurrencyPair(AUD, JPY);
    private static final CurrencyPair AUDUSD = new CurrencyPair(AUD, USD);

    private static final CurrencyPair EURAUD = new CurrencyPair(EUR, AUD);
    private static final CurrencyPair EURNOK = new CurrencyPair(EUR, NOK);
    private static final CurrencyPair EURUSD = new CurrencyPair(EUR, USD);

    private static final CurrencyPair JPYUSD = new CurrencyPair(JPY, USD);
    private static final CurrencyPair JPYAUD = new CurrencyPair(JPY, AUD);

    private static final CurrencyPair NOKAUD = new CurrencyPair(NOK, AUD);

    private static final CurrencyPair USDAUD = new CurrencyPair(USD, AUD);
    private static final CurrencyPair USDNOK = new CurrencyPair(USD, NOK);
    private static final CurrencyPair USDJPY = new CurrencyPair(USD, JPY);

    private static final CurrencyPair OMGWTF = new CurrencyPair(OMG, WTF);

    private RateCalculator<BigDecimal> sut;

    @Before
    public void setUp() {
        SimpleReferenceData referenceData = new SimpleReferenceData(1);
        Map<CurrencyPair, Optional<BigDecimal>> rates = new LinkedHashMap<>();
        Arithmetic<BigDecimal> arithmetic = new BigDecimalArithmetic(4, RoundingMode.HALF_UP);

        referenceData.addFeedCurrencyPair(AUDUSD);
        referenceData.addFeedCurrencyPair(EURNOK);
        referenceData.addFeedCurrencyPair(EURUSD);
        referenceData.addFeedCurrencyPair(USDJPY);
        referenceData.addSplitCurrency(AUDEUR, USD);
        referenceData.addSplitCurrency(AUDJPY, USD);
        referenceData.addSplitCurrency(AUDNOK, USD);
        referenceData.addSplitCurrency(USDNOK, EUR);

        rates.put(AUDUSD, Optional.of(new BigDecimal("0.7")));
        rates.put(EURNOK, Optional.of(new BigDecimal("8.7")));
        rates.put(EURUSD, Optional.of(new BigDecimal("1.25")));
        rates.put(USDJPY, Optional.of(new BigDecimal("105")));

        sut = new RateCalculator<>(
                referenceData,
                currencyPair -> rates.getOrDefault(currencyPair, Optional.empty()),
                arithmetic);
    }

    @Test
    public void testGetRate_whenDirectRateAvailable_thenUseDirectRate() {
        assertEquals(Optional.of(new BigDecimal("0.7")), sut.getRate(AUDUSD));
        assertEquals(Optional.of(new BigDecimal("105")), sut.getRate(USDJPY));
    }

    @Test
    public void testGetRate_whenInvertedRateAvailable_thenUseInvertedRate() {
        assertEquals(Optional.of(new BigDecimal("1.4286")), sut.getRate(USDAUD));
        assertEquals(Optional.of(new BigDecimal("0.0095")), sut.getRate(JPYUSD));
    }

    @Test
    public void testGetRate_whenCrossRateAvailable_andBothSplitRatesAreDirect_thenCalculateCrossRate() {
        assertEquals(Optional.of(new BigDecimal("73.5")), sut.getRate(AUDJPY));
        assertEquals(Optional.of(new BigDecimal("0.0136")), sut.getRate(JPYAUD));
    }

    @Test
    public void testGetRate_whenCrossRateAvailable_andOneSplitRateIsInverted_thenCalculateCrossRate() {
        assertEquals(Optional.of(new BigDecimal("0.56")), sut.getRate(AUDEUR));
        assertEquals(Optional.of(new BigDecimal("1.7858")), sut.getRate(EURAUD));
    }

    @Test
    public void testGetRate_whenCrossRateAvailableViaDoubleCross_thenCalculateCrossRate() {
        assertEquals(Optional.of(new BigDecimal("4.872")), sut.getRate(AUDNOK));
        assertEquals(Optional.of(new BigDecimal("0.2051")), sut.getRate(NOKAUD));
    }

    @Test
    public void testGetRate_whenNoRateAvailable_thenReturnEmpty() {
        assertEquals(Optional.empty(), sut.getRate(OMGWTF));
    }
}
