package ua.lysenko.andrii.exchange;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.LinkedList;
import java.util.List;

public class Utils {

    public static List<SelenideElement> getListFromElements(ElementsCollection elements) {
        Iterable<SelenideElement> it = elements.asDynamicIterable();
        List<SelenideElement> rows = new LinkedList<>();
        it.forEach(rows::add);
        return rows;
    }
}
