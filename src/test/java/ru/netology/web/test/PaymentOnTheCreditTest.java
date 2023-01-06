package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.OrderCardPage;

import static com.codeborne.selenide.Selenide.open;

@Epic("Покупка тура по карте в кредит")
public class PaymentOnTheCreditTest {
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

    //---ВЫПАДАЮЩИЕ СООБЩЕНИЯ НА ОТВЕТ ОТ БАНКОВСКИХ СЕРВИСОВ---
    @Feature("Выпадающие сообщения")
    @Test
    @DisplayName("Проверяем выпадающее сообщение при покупке тура в кредит, когда банк одобрил кредит")
    void shouldSuccessfulSendTheFormOnThePaymentOnTheCredit() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("4444444444444442");
        var cardPaymentOnCreditPage = orderCardPage.goToPaymentOnCreditPage();
        var formPage = cardPaymentOnCreditPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
        formPage.notificationOk();
    }

    @Feature("Выпадающие сообщения")
    @Test
    @DisplayName("Проверяем выпадающее сообщение при покупке тура в кредит, когда банк отказал в выдаче кредита")
    void shouldBeDeniedPaymentOnTheCredit() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("4444444444444442");
        var cardPaymentOnCreditPage = orderCardPage.goToPaymentOnCreditPage();
        var formPage = cardPaymentOnCreditPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
        formPage.notificationError();
    }
}
