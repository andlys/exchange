package ua.lysenko.andrii.exchange;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ua.lysenko.andrii.exchange.pages.CalculatorPage;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
@Category(CalculatorPageTest.class)
public class CountySelectionTest {

    CalculatorPage calculatorPage;
    private String country;
    private String currency;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "Poland", "PLN"}, {"Spain", "EUR"}, {"Romania", "RON" }
        });
    }

    public CountySelectionTest(String country, String currency) {
        this.country = country;
        this.currency = currency;
    }

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
    public void givenSiteOpensWhenCountryIsChosenThenCurrencyIsChanged() {
        calculatorPage.chooseCountry(country);
        Assert.assertEquals(country, calculatorPage.getActiveCountry());
        Assert.assertEquals(currency, calculatorPage.getActiveCurrency());
    }
}
