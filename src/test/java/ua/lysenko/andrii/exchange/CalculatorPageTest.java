package ua.lysenko.andrii.exchange;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.lysenko.andrii.exchange.pages.CalculatorPage;
import ua.lysenko.andrii.exchange.pages.RatesTable;

import java.math.BigDecimal;
import java.util.Optional;

public class CalculatorPageTest {

    CalculatorPage calculatorPage;
    Logger log = LoggerFactory.getLogger(CalculatorPageTest.class);

    @Before
    public void before() {
        calculatorPage = new CalculatorPage();
        Selenide.open(Config.BASE_URL);
    }

    @After
    public void after() {
        WebDriverRunner.getWebDriver().close();
    }

    @Test
    public void verifySiteOpensAndHeadingIsCorrect() {
        String actualText = calculatorPage.scrollToRates()
                .getHeadingText();
        Assert.assertEquals("Online currency exchange calculator", actualText);
    }

    @Test
    public void givenSellInputFilledWhenBuyInputFilledThenSellInputShouldBeEmpty() {
        boolean isSellEmpty = calculatorPage.scrollToRates()
                .fillSellAmount(100)
                .fillBuyAmount(200)
                .isSellAmountEmpty();
        Assert.assertTrue("Sell input should be empty", isSellEmpty);
    }

    @Test
    public void givenBuyInputFilledWhenSellInputFilledThenBuyInputShouldBeEmpty() {
        boolean isBuyEmpty = calculatorPage.scrollToRates()
                .fillBuyAmount(200)
                .fillSellAmount(100)
                .isBuyAmountEmpty();
        Assert.assertTrue("Buy input should be empty", isBuyEmpty);
    }

    @Test
    public void givenSiteOpensThenTableContains32Rows() {
        RatesTable ratesTable = calculatorPage.waitUntilRatesAreLoaded()
                .getRatesTable();
        Assert.assertEquals(32, ratesTable.getRows().size());
    }

    @Test
    public void givenSiteOpensThenTableContainsUsdDollarAndCzechKoruna() {
        RatesTable ratesTable = calculatorPage.waitUntilRatesAreLoaded()
                .getRatesTable();
        Assert.assertTrue(ratesTable.getRows().stream().anyMatch(row -> "CZK (Czech Koruna)".equals(row.getCurrency())));
        Assert.assertTrue(ratesTable.getRows().stream().anyMatch(row -> "USD (US Dollar)".equals(row.getCurrency())));
    }

    @Test
    public void givenSiteAmountLessThanBankAmountThenLossShouldBeDisplayedForSingleRow() {
        RatesTable ratesTable = calculatorPage.waitUntilRatesAreLoaded()
                .getRatesTable();
        Optional<RatesTable.RatesTableRow> rowWithLossOptional = ratesTable.getRows().stream().filter(row -> {
            double siteAmount = Double.valueOf(row.getSiteAmount());
            String swedebankText = row.getSwedebankAmount();
            if (swedebankText == null || swedebankText.isEmpty() || swedebankText.equals("-"))
                return false;
            double swedebankAmount = Double.valueOf(swedebankText);
            return siteAmount > swedebankAmount;
        }).findFirst();
        Assert.assertTrue(rowWithLossOptional.isPresent());
        RatesTable.RatesTableRow row = rowWithLossOptional.get();
        log.info("Tested currency: {}", row.getCurrency());
        Double expectedLoss = BigDecimal.valueOf(Double.valueOf(row.getSwedebankAmount()))
                .subtract(BigDecimal.valueOf(Double.valueOf(row.getSiteAmount())))
                        .doubleValue();
        Double actualLoss = row.getSwedebankAmountNegative().isEmpty() ? 0D: Double.valueOf(row.getSwedebankAmountNegative());
        Assert.assertEquals("Loss amount should be correct", expectedLoss, actualLoss);
    }

    @Test
    public void givenSiteAmountLessThanBankAmountThenLossShouldBeDisplayedForAllRows() {
        RatesTable ratesTable = calculatorPage.waitUntilRatesAreLoaded()
                .getRatesTable();
        ratesTable.getRows().stream().filter(row -> {
            row.scrollTo();
            double siteAmount = Double.valueOf(row.getSiteAmount());
            String swedebankText = row.getSwedebankAmount();
            if (swedebankText == null || swedebankText.isEmpty() || swedebankText.equals("-"))
                return false;
            double swedebankAmount = Double.valueOf(swedebankText);
            return siteAmount > swedebankAmount;
        }).forEach( row -> {
            log.info("Tested currency: {}", row.getCurrency());
            Double expectedLoss = BigDecimal.valueOf(Double.valueOf(row.getSwedebankAmount()))
                    .subtract(BigDecimal.valueOf(Double.valueOf(row.getSiteAmount())))
                    .doubleValue();
            Double actualLoss = row.getSwedebankAmountNegative().isEmpty() ? 0D : Double.valueOf(row.getSwedebankAmountNegative());
            Assert.assertEquals("Loss amount should be correct", expectedLoss, actualLoss);
        });
    }

}
