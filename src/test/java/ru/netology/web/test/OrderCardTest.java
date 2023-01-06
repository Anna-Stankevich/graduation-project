package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.web.page.OrderCardPage;

import static com.codeborne.selenide.Selenide.open;

@Epic("Карточка тура")
public class OrderCardTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setupTest() {
        open(System.getProperty("application.address"));
    }

    //---ФУНКЦИОНАЛ КНОПОК НА СТРАНИЦЕ---
    @Feature("Кнопки 'Купить' и 'Купить в кредит'")
    @Test
    @DisplayName("Проверяем статус кнопок на изначальной карточке покупки тура (обе должны быть красного цвета)")
    void shouldCheckButtonStatusOnTheOrderCard() {
        var orderCardPage = new OrderCardPage();
        orderCardPage.buttonStatusExtraStart();
    }

    @Feature("Кнопки 'Купить' и 'Купить в кредит'")
    @Test
    @DisplayName("Проверяем статус кнопок при клике на каждую (та, на которую кликнули должна быть белой, другая красной)")
    void shouldCheckButtonsStatus() {
        var orderCardPage = new OrderCardPage();
        orderCardPage.buttonStatusExtraPayment();
        orderCardPage.buttonStatusExtraPaymentOnCredit();
    }

    @Feature("Кнопки 'Купить' и 'Купить в кредит'")
    @Test
    @DisplayName("Проверяем, что при нажатии на конкретную кнопку переходим к нужной форме. Сначала к одной, потом к другой")
    void shouldGoesToTheDesiredFormOnButtonClick() {
        var orderCardPage = new OrderCardPage();
        orderCardPage.goToPaymentPage();
        orderCardPage.goToPaymentOnCreditPage();
    }
}
