package ua.lysenko.andrii.exchange.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ua.lysenko.andrii.exchange.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class RatesTable {

    SelenideElement root;
    By rowLocator = By.cssSelector("tr");

    public RatesTable(SelenideElement root) {
        this.root = root;
    }

    public List<RatesTableRow> getRows() {
        return Utils.getListFromElements(root.$$(rowLocator).shouldHave(CollectionCondition.sizeGreaterThan(1)))
                .stream().skip(1).map(RatesTableRow::new)
                .collect(Collectors.toList());
    }

    public RatesTableRow getRow(int num) {
        return getRows().get(num);
    }

    public static class RatesTableRow {
        SelenideElement root;
        By currencyLocator = By.cssSelector("td:nth-child(1)");
        By siteAmountLocator = By.cssSelector("td:nth-child(4)");
        By swedebankAmountLocator = By.cssSelector("td:nth-child(5)");

        public RatesTableRow(SelenideElement root) {
            this.root = root;
        }

        public String getCurrency() {
            return root.$(currencyLocator).getText();
        }

        public String getSiteAmount() {
            return root.$(siteAmountLocator).getText().replace(",", "");
        }

        public String getSwedebankAmount() {
            return root.$(swedebankAmountLocator).getText().split("\\n")[0].replace(",", "");
        }

        public String getSwedebankAmountNegative() {
            String[] amounts = root.$(swedebankAmountLocator).getText().split("\\n");
            return (amounts.length > 1) ? amounts[1].replace(")", "").replace("(", "") : "";
        }

        public void scrollTo() {
            root.scrollTo();
        }
    }
}
