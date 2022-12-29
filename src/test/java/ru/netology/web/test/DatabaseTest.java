package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.OrderCardPage;
import ru.netology.web.repository.CreditRequestEntityRepositoryMySQL;
import ru.netology.web.repository.CreditRequestEntityRepositoryPostgreSQL;
import ru.netology.web.repository.PaymentEntityRepositoryMySQL;
import ru.netology.web.repository.PaymentEntityRepositoryPostgreSQL;

import static com.codeborne.selenide.Selenide.open;

@Epic("Работа с базой данных")
public class DatabaseTest {
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

    //---БАЗА ДАННЫХ---
    @Feature("База данных MySQL")
    @Story("Успешные операции")
    @Test
    @DisplayName("Проверяем, что приложение сохранет в своей БЗ успешно совершенный платеж по карте")
    @SneakyThrows
    void shouldStoreTheSuccessfullyTheCompletedCardPaymentInTheDatabase() {
        var rep = new PaymentEntityRepositoryMySQL();
        int count = rep.getStatusCount("APPROVED");

        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
        formPage.activeNotification();

        int actualCount = rep.getStatusCount("APPROVED");

        Assertions.assertEquals(count + 1, actualCount);
    }

    @Feature("База данных MySQL")
    @Story("Успешные операции")
    @Test
    @DisplayName("Проверяем, что приложение сохранет в своей БЗ успешно совершенную покупку в кредит")
    @SneakyThrows
    void shouldStoreTheSuccessfullyTheCompletedPaymentOnTheCreditInTheDatabase() {
        var rep = new CreditRequestEntityRepositoryMySQL();
        int count = rep.getStatusCount("APPROVED");

        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var cardPaymentOnCreditPage = orderCardPage.goToPaymentOnCreditPage();
        var formPage = cardPaymentOnCreditPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
        formPage.activeNotification();

        int actualCount = rep.getStatusCount("APPROVED");

        Assertions.assertEquals(count + 1, actualCount);
    }

    @Feature("База данных MySQL")
    @Story("Отказ в операции")
    @Test
    @DisplayName("Проверяем, что приложение сохранет в своей БЗ отказ в совершении платежа по карте")
    @SneakyThrows
    void shouldStoreTheDenialOfCardPaymentInTheDatabase() {
        var rep = new PaymentEntityRepositoryMySQL();
        int count = rep.getStatusCount("DECLINED");

        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("4444444444444442");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
        formPage.activeNotification();

        int actualCount = rep.getStatusCount("DECLINED");

        Assertions.assertEquals(count + 1, actualCount);

    }

    @Feature("База данных MySQL")
    @Story("Отказ в операции")
    @Test
    @DisplayName("Проверяем, что приложение сохранет в своей БЗ отказ в совершении покупке в кредит")
    @SneakyThrows
    void shouldStoreTheDenialOfPaymentOnTheCreditInTheDatabase() {
        var rep = new CreditRequestEntityRepositoryMySQL();
        int count = rep.getStatusCount("DECLINED");

        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("4444444444444442");
        var cardPaymentOnCreditPage = orderCardPage.goToPaymentOnCreditPage();
        var formPage = cardPaymentOnCreditPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
        formPage.activeNotification();

        int actualCount = rep.getStatusCount("DECLINED");

        Assertions.assertEquals(count + 1, actualCount);
    }

    @Feature("База данных PostgreSQL")
    @Story("Успешные операции")
    @Test
    @DisplayName("Проверяем, что приложение сохранет в своей БЗ успешно совершенный платеж по карте")
    @SneakyThrows
    void shouldStoreTheSuccessfullyTheCompletedCardPaymentInTheDatabaseP() {
        var rep = new PaymentEntityRepositoryPostgreSQL();
        int count = rep.getStatusCount("APPROVED");

        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
        formPage.activeNotification();

        int actualCount = rep.getStatusCount("APPROVED");

        Assertions.assertEquals(count + 1, actualCount);
    }

    @Feature("База данных PostgreSQL")
    @Story("Успешные операции")
    @Test
    @DisplayName("Проверяем, что приложение сохранет в своей БЗ успешно совершенную покупку в кредит")
    @SneakyThrows
    void shouldStoreTheSuccessfullyTheCompletedPaymentOnTheCreditInTheDatabaseP() {
        var rep = new CreditRequestEntityRepositoryPostgreSQL();
        int count = rep.getStatusCount("APPROVED");

        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var cardPaymentOnCreditPage = orderCardPage.goToPaymentOnCreditPage();
        var formPage = cardPaymentOnCreditPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
        formPage.activeNotification();

        int actualCount = rep.getStatusCount("APPROVED");

        Assertions.assertEquals(count + 1, actualCount);
    }

    @Feature("База данных PostgreSQL")
    @Story("Отказ в операции")
    @Test
    @DisplayName("Проверяем, что приложение сохранет в своей БЗ отказ в совершении платежа по карте")
    @SneakyThrows
    void shouldStoreTheDenialOfCardPaymentInTheDatabaseP() {
        var rep = new PaymentEntityRepositoryPostgreSQL();
        int count = rep.getStatusCount("DECLINED");

        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("4444444444444442");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
        formPage.activeNotification();

        int actualCount = rep.getStatusCount("DECLINED");

        Assertions.assertEquals(count + 1, actualCount);

    }

    @Feature("База данных PostgreSQL")
    @Story("Отказ в операции")
    @Test
    @DisplayName("Проверяем, что приложение сохранет в своей БЗ отказ в совершении покупке в кредит")
    @SneakyThrows
    void shouldStoreTheDenialOfPaymentOnTheCreditInTheDatabaseP() {
        var rep = new CreditRequestEntityRepositoryPostgreSQL();
        int count = rep.getStatusCount("DECLINED");

        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("4444444444444442");
        var cardPaymentOnCreditPage = orderCardPage.goToPaymentOnCreditPage();
        var formPage = cardPaymentOnCreditPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
        formPage.activeNotification();

        int actualCount = rep.getStatusCount("DECLINED");

        Assertions.assertEquals(count + 1, actualCount);
    }

}
