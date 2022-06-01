package ua.lysenko.andrii.exchange.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

public class CalculatorPage {

    SelenideElement heading = Selenide.$("#rates");
    SelenideElement panel = Selenide.$("#currency-exchange-app");
    SelenideElement currencySelected = Selenide.$x("//div[@class='form-group' and ./label[text()='Sell']]//span");
    SelenideElement buyAmountInput = Selenide.$(".form-group > input[data-ng-model*=to_amount]");
    SelenideElement sellAmountInput = Selenide.$(".form-group > input[data-ng-model*=from_amount]");
    SelenideElement countryFooterIcon = Selenide.$("div.footer-bottom span[role=button]");
    SelenideElement countryOpenerButton = Selenide.$("div[class='dropup'] button");
    SelenideElement countryPopupDropdown = Selenide.$("div[class='dropup open'] ul");
    SelenideElement spinner = Selenide.$("[data-ng-show='currencyExchangeVM.loading']");
    SelenideElement ratesTableRoot = Selenide.$("table.table");

    public CalculatorPage scrollToRates() {
        panel.shouldBe(Condition.visible).scrollTo();
        return this;
    }

    public CalculatorPage fillBuyAmount(int amount) {
        buyAmountInput.sendKeys(String.valueOf(amount));
        return this;
    }

    public CalculatorPage fillSellAmount(int amount) {
        sellAmountInput.sendKeys(String.valueOf(amount));
        return this;
    }

    public boolean isBuyAmountEmpty() {
        return buyAmountInput.getValue().isEmpty();
    }

    public boolean isSellAmountEmpty() {
        return sellAmountInput.getValue().isEmpty();
    }

    public String getHeadingText() {
        return heading.shouldBe(Condition.visible).text();
    }

    public void chooseCountry(String country) {
        countryFooterIcon.scrollTo().click();
        countryOpenerButton.shouldBe(Condition.visible, Duration.ofSeconds(5))
                .click();
        countryPopupDropdown.shouldBe(Condition.visible)
                .$$("li")
                .findBy(Condition.text(country))
                .click();
    }

    public String getActiveCountry() {
        countryFooterIcon.click();
        String country = countryOpenerButton.shouldBe(Condition.visible, Duration.ofSeconds(5))
                .getText();
        countryFooterIcon.click();
        return country;
    }

    public String getActiveCurrency() {
        waitUntilRatesAreLoaded();
        return currencySelected.shouldBe(Condition.visible).getText();
    }

    public CalculatorPage waitUntilRatesAreLoaded() {
        scrollToRates();
        spinner.shouldBe(Condition.not(Condition.visible), Duration.ofSeconds(10));
        return this;
    }

    public RatesTable getRatesTable() {
        return new RatesTable(ratesTableRoot.shouldBe(Condition.visible));
    }
}
