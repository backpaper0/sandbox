package sample;

import org.junit.Test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;

public class CalcTest {

    @Test
    public void test() throws Exception {
        CalcPage page = Selenide.open("http://localhost:8080/", CalcPage.class);
        page.a.val("23");
        page.b.val("45");
        page.calc.click();
        page.answer.shouldBe(Condition.text("68"));
        Selenide.close();
    }
}
