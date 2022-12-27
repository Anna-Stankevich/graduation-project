package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.OrderCardPage;
import ru.netology.web.repository.CreditRequestEntityRepositorySQL;
import ru.netology.web.repository.PaymentEntityRepositorySQL;

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
//    @Feature("База данных")
//    @Story("Успешные операции")
//    @Test
//    @DisplayName("Проверяем, что приложение сохранет в своей БЗ успешно совершенный платеж по карте")
//    void shouldStoreTheSuccessfullyTheCompletedCardPaymentInTheDatabase() {
//        var dataSQL = "SELECT COUNT(id) FROM payment_entity WHERE status = 'APPROVED'"; // запрос
//        var runner = new QueryRunner(); // создаем новый исполнитель запросов
//        try (var conn = DriverManager.getConnection("jdbc:mysql://185.119.57.47:3306/app", "app", "pass");) {// создаем исключение и как его ловить
//
//            Long count = runner.query(conn, dataSQL, new ScalarHandler<Long>());
//
//
//            var OrderCardPage = new OrderCardPage();
//            var formFieldsInfo = DataHelper.getValidFieldSet();
//            var CardPaymentPage = OrderCardPage.goToPaymentPage();
//            var FormPage = CardPaymentPage.goToFormPage();
//            FormPage.goToNotificationPage(formFieldsInfo);
//            FormPage.notificationOk();
//            Long actualCount = runner.query(conn, dataSQL, new ScalarHandler<Long>());
//            Assertions.assertEquals(count.intValue() + 1, actualCount.intValue());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Feature("База данных")
//    @Story("Успешные операции")
//    @Test
//    @DisplayName("Проверяем, что приложение сохранет в своей БЗ успешно совершенную покупку в кредит")
//    void shouldStoreTheSuccessfullyTheCompletedPaymentOnTheCreditInTheDatabase() {
//        var dataSQL = "SELECT COUNT(id) FROM credit_request_entity WHERE status = 'APPROVED'";
//        var runner = new QueryRunner();
//        try (var conn = DriverManager.getConnection("jdbc:mysql://185.119.57.47:3306/app", "app", "pass");) {
//            Long count = runner.query(conn, dataSQL, new ScalarHandler<Long>());
//            var OrderCardPage = new OrderCardPage();
//            var formFieldsInfo = DataHelper.getValidFieldSet();
//            var CardPaymentOnCreditPage = OrderCardPage.goToPaymentOnCreditPage();
//            var FormPage = CardPaymentOnCreditPage.goToFormPage();
//            FormPage.goToNotificationPage(formFieldsInfo);
//            FormPage.activeNotification();
//            Long actualCount = runner.query(conn, dataSQL, new ScalarHandler<Long>());
//            Assertions.assertEquals(count.intValue() + 1, actualCount.intValue());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Feature("База данных")
//    @Story("Отказ в операции")
//    @Test
//    @DisplayName("Проверяем, что приложение сохранет в своей БЗ отказ в совершении платежа по карте")
//    void shouldStoreTheDenialOfCardPaymentInTheDatabase() {
//        var dataSQL = "SELECT COUNT(id) FROM payment_entity WHERE status = 'DECLINED'";
//        var runner = new QueryRunner();
//        try (var conn = DriverManager.getConnection("jdbc:mysql://185.119.57.47:3306/app", "app", "pass");) {
//            Long count = runner.query(conn, dataSQL, new ScalarHandler<Long>());
//            var OrderCardPage = new OrderCardPage();
//            var formFieldsInfo = DataHelper.getValidFieldSet();
//            formFieldsInfo.setCardNumber("4444444444444442");
//            var CardPaymentPage = OrderCardPage.goToPaymentPage();
//            var FormPage = CardPaymentPage.goToFormPage();
//            FormPage.goToNotificationPage(formFieldsInfo);
//            Long actualCount = runner.query(conn, dataSQL, new ScalarHandler<Long>());
//            Assertions.assertEquals(count.intValue() + 1, actualCount.intValue());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Feature("База данных")
//    @Story("Отказ в операции")
//    @Test
//    @DisplayName("Проверяем, что приложение сохранет в своей БЗ отказ в совершении покупке в кредит")
//    void shouldStoreTheDenialOfPaymentOnTheCreditInTheDatabase() {
//        var dataSQL = "SELECT COUNT(id) FROM credit_request_entity WHERE status = 'DECLINED'";
//        var runner = new QueryRunner();
//        try (var conn = DriverManager.getConnection("jdbc:mysql://185.119.57.47:3306/app", "app", "pass");) {
//            Long count = runner.query(conn, dataSQL, new ScalarHandler<Long>());
//            var OrderCardPage = new OrderCardPage();
//            var formFieldsInfo = DataHelper.getValidFieldSet();
//            formFieldsInfo.setCardNumber("4444444444444442");
//            var CardPaymentOnCreditPage = OrderCardPage.goToPaymentOnCreditPage();
//            var FormPage = CardPaymentOnCreditPage.goToFormPage();
//            FormPage.goToNotificationPage(formFieldsInfo);
//            Long actualCount = runner.query(conn, dataSQL, new ScalarHandler<Long>());
//            Assertions.assertEquals(count.intValue() + 1, actualCount.intValue());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Feature("База данных")
    @Story("Успешные операции")
    @Test
    @DisplayName("Проверяем, что приложение сохранет в своей БЗ успешно совершенный платеж по карте")
    @SneakyThrows
    void shouldStoreTheSuccessfullyTheCompletedCardPaymentInTheDatabase() {
        var rep = new PaymentEntityRepositorySQL();
        int count = rep.getStatusCount("APPROVED");

        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
        formPage.notificationOk();

        int actualCount = rep.getStatusCount("APPROVED");

        Assertions.assertEquals(count + 1, actualCount);
    }

    @Feature("База данных")
    @Story("Успешные операции")
    @Test
    @DisplayName("Проверяем, что приложение сохранет в своей БЗ успешно совершенную покупку в кредит")
    @SneakyThrows
    void shouldStoreTheSuccessfullyTheCompletedPaymentOnTheCreditInTheDatabase() {
        var rep = new CreditRequestEntityRepositorySQL();
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

    @Feature("База данных")
    @Story("Отказ в операции")
    @Test
    @DisplayName("Проверяем, что приложение сохранет в своей БЗ отказ в совершении платежа по карте")
    @SneakyThrows
    void shouldStoreTheDenialOfCardPaymentInTheDatabase() {
        var rep = new PaymentEntityRepositorySQL();
        int count = rep.getStatusCount("DECLINED");

        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("4444444444444442");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);

        int actualCount = rep.getStatusCount("DECLINED");

        Assertions.assertEquals(count + 1, actualCount);

    }

    @Feature("База данных")
    @Story("Отказ в операции")
    @Test
    @DisplayName("Проверяем, что приложение сохранет в своей БЗ отказ в совершении покупке в кредит")
    @SneakyThrows
    void shouldStoreTheDenialOfPaymentOnTheCreditInTheDatabase() {
        var rep = new CreditRequestEntityRepositorySQL();
        int count = rep.getStatusCount("DECLINED");

        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("4444444444444442");
        var cardPaymentOnCreditPage = orderCardPage.goToPaymentOnCreditPage();
        var formPage = cardPaymentOnCreditPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);

        int actualCount = rep.getStatusCount("DECLINED");

        Assertions.assertEquals(count + 1, actualCount);
    }

}
