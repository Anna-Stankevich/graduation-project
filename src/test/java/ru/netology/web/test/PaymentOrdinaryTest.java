package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.OrderCardPage;

import static com.codeborne.selenide.Selenide.open;

@Epic("Покупка тура по карте")
public class PaymentOrdinaryTest {
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
        open("http://185.119.57.47:8080");
    }

    //---ВЫПАДАЮЩИЕ СООБЩЕНИЯ НА ОТВЕТ ОТ БАНКОВСКИХ СЕРВИСОВ---
    @Feature("Выпадающие сообщения")
    @Test
    @DisplayName("Проверяем выпадающее сообщение при покупке тура по карте, когда банк одобрил покупку")
    void shouldSuccessfulSendTheFormOnThePayment() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
        formPage.notificationOk();
    }

    @Feature("Выпадающие сообщения")
    @Test
    @DisplayName("Проверяем выпадающее сообщение при покупке тура по карте, когда банк отказал в покупке")
    void shouldBeDeniedPayment() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("4444444444444442");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
        formPage.notificationError();
    }
}
