import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class AppCardDeliveryTest {

    @BeforeEach
    void setUpTests() {
        open("http://localhost:9999");
    }

    public String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    String planningDate = generateDate(3, "dd.MM.yyyy");

    @Test
    void shouldSendForm() {
        $("[data-test-id='city' ] input").setValue("Самара");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Гусарский Гусь");
        $("[data-test-id='phone'] input").setValue("+79023004000");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='notification']").shouldHave(text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(12));
        $("[data-test-id='notification']").shouldBe(visible);
    }

    @Test
    void shouldSendFormWithDashInName() {
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Гусарский-Павловский Гусь Хрустальный");
        $("[data-test-id=phone] input").setValue("+79023004000");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='notification']").shouldHave(text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(12));
        $("[data-test-id='notification']").shouldBe(visible);
    }

    @Test
    void shouldSendFormWithLetterYoInName() {
        $("[data-test-id=city] input").setValue("Саратов");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Гусарский Гусёнок");
        $("[data-test-id=phone] input").setValue("+79023004000");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).should(visible);
        $("[data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(12));
    }

    @Test
    void shouldSendFormWithLetterName() {
        $("[data-test-id=city] input").setValue("Саратов");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("123 Гусь");
        $("[data-test-id=phone] input").setValue("+79023004000");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).should(visible);
        $("[data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(12));
    }

    @Test
    void shouldSendFormWithLetterInName() {
        $("[data-test-id=city] input").setValue("Саратов");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("RtuDtu");
        $("[data-test-id=phone] input").setValue("+79023004000");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).should(visible);
        $("[data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(12));
    }

    @Test
    void shouldSendFormWithLetterPhone() {
        $("[data-test-id=city] input").setValue("Саратов");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Иван");
        $("[data-test-id=phone] input").setValue("+790230040005");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.")).should(visible);
        $("[data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(12));
    }

    @Test
    void shouldCardFormWithoutCheckbox() {
        $("[data-test-id=city] input").setValue("Новосибирск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Иван");
        $("[data-test-id=phone] input").setValue("+79023004000");
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Я соглашаюсь с условиями обработки и использования моих персональных данных")).should(visible);
        $("[data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(12));
    }

    @Test
    void shouldCardFormWithoutNoCity() {
        $("[data-test-id=city] input").setValue("");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Иван");
        $("[data-test-id=phone] input").setValue("+79023004000");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).should(visible);
        $("[data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(12));
    }

    @Test
    void shouldCardFormWithoutCity() {
        $("[data-test-id=city] input").setValue("Ново");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Гусь");
        $("[data-test-id=phone] input").setValue("+79023004000");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).should(visible);
        $("[data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(12));
    }
}
