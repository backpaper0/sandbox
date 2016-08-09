package sample;

import org.openqa.selenium.support.FindBy;

import com.codeborne.selenide.SelenideElement;

public class CalcPage {

    @FindBy(name = "a")
    SelenideElement a;
    @FindBy(name = "b")
    SelenideElement b;
    @FindBy(id = "calc")
    SelenideElement calc;
    @FindBy(id = "answer")
    SelenideElement answer;
}
