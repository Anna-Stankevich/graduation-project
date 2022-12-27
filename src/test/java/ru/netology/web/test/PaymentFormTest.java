package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.OrderCardPage;

import static com.codeborne.selenide.Selenide.open;

@Epic("Форма покупки")
public class PaymentFormTest {
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

    //---ОБЩЕЕ---
    @Feature("Общее")
    @Story("Пустая форма")
    @Test
    @DisplayName("Проверяем, что пустая форма не будет отправлена на сервер")
    void shouldNotSubmitAnEmptyForm() {
        var orderCardPage = new OrderCardPage();
        var cardPaymentOnCreditPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentOnCreditPage.goToFormPage();
        formPage.formNotSend();
    }

    //---ФУНКЦИОНАЛ КНОПКИ НА СТРАНИЦЕ---
    @Feature("Общее")
    @Story("Кнопка 'Продолжить'")
    @Test
    @DisplayName("Проверяем, что, введя в форму валидные данные, нажав на кнопку «Продолжить» она на некоторое время становится некликабельной")
    void shouldNotBeClickableButtonAfterClick() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
    }

    //---ВАЛИДАЦИЯ ПОЛЕЙ ФОРМЫ---
    //---ПОЛЕ НОМЕРА КАРТЫ---
    @Feature("Валидация полей")
    @Story("Поле номера карты")
    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/InvalidValuesCardNumber.csv")
    @DisplayName("Проверяем, что в поле карты нельзя ввести некоторые невалидные значения")
    void shouldCheckWhatCannotBeEnteredInTheCardNumberFieldSomeInvalidValues(String text) {
        var orderCardPage = new OrderCardPage();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.setFieldValue("Номер карты", text);
        String expected = "";
        String actual = formPage.getFieldValue("Номер карты");
        Assertions.assertEquals(expected, actual);
    }

    @Feature("Форма")
    @Story("Поле номера карты")
    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле карты не заполнено")
    void shouldShowAnErrorMessageBelowTheCardNumberField_EmptyField() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("Номер карты", "Поле обязательно для заполнения");
    }

    @Feature("Форма")
    @Story("Поле номера карты")
    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле карты заполнено нулями")
    void shouldShowAnErrorMessageBelowTheCardNumberField_OnlyZero() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("0000000000000000");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("Номер карты", "Неверный формат");
    }

    @Feature("Форма")
    @Story("Поле номера карты")
    @Test
    @DisplayName("Проверяем, что введение пробела в поле карты игнорируется")
    void shouldBeSpaceIgnored() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("4444 4444 4444 4441");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
    }

    @Feature("Форма")
    @Story("Поле номера карты")
    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле карты заполнено 15-ю символами")
    void shouldShowAnErrorMessageBelowTheCardNumberFieldWhenEntered15Symbol() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("444444444444441");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("Номер карты", "Неверный формат");

    }

    @Feature("Форма")
    @Story("Поле номера карты")
    @Test
    @DisplayName("Проверяем, что нельзя ввести в поле карты более 16-ти цифр")
    void canOnlyEnter16Digits() {
        var orderCardPage = new OrderCardPage();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.setFieldValue("Номер карты", "44444444444444441");
        String expected = "4444 4444 4444 4444";
        String actual = formPage.getFieldValue("Номер карты");
        Assertions.assertEquals(expected, actual);
    }

    //---ПОЛЕ МЕСЯЦА---
    @Feature("Форма")
    @Story("Поле месяца")
    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/InvalidValuesMonth.csv")
    @DisplayName("Проверяем, что в поле месяца нельзя ввести некоторые невалидные значения")
    void shouldCheckWhatCannotBeEnteredInTheMonthFieldSomeInvalidValues(String text) {
        var orderCardPage = new OrderCardPage();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.setFieldValue("Месяц", text);
        String expected = "";
        String actual = formPage.getFieldValue("Месяц");
        Assertions.assertEquals(expected, actual);
    }

    @Feature("Форма")
    @Story("Поле месяца")
    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле месяца не заполнено")
    void shouldShowAnErrorMessageBelowTheMonthField_EmptyField() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setMonth("");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("Месяц", "Поле обязательно для заполнения");
    }

    @Feature("Форма")
    @Story("Поле месяца")
    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле месяца заполнено нулями")
    void shouldShowAnErrorMessageBelowTheMonthField_OnlyZero() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setMonth("00");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("Месяц", "Неверный формат");
    }

    @Feature("Форма")
    @Story("Поле месяца")
    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле месяца заполнено одним символом")
    void shouldShowAnErrorMessageBelowTheMonthFieldWhenEnteredOneSymbol() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setMonth("3");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("Месяц", "Неверный формат");
    }

    @Feature("Форма")
    @Story("Поле месяца")
    @Test
    @DisplayName("Проверяем, что нельзя ввести в поле месяц более 2-х цифр")
    void canOnlyEnter2DigitsTheMonthField() {
        var orderCardPage = new OrderCardPage();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.setFieldValue("Месяц", "011");
        String expected = "01";
        String actual = formPage.getFieldValue("Месяц");
        Assertions.assertEquals(expected, actual);
    }

    @Feature("Форма")
    @Story("Поле месяца")
    @Test
    @DisplayName("Проверяем, что в поле месяца нельзя указать несуществующий месяцев")
    void shouldShowAnErrorMessageBelowTheMonthField_13Month() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setMonth("13");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("Месяц", "Неверно указан срок действия карты");
    }

    @Feature("Форма")
    @Story("Поле месяца")
    @Test
    @DisplayName("Проверяем выпадающее сообщения об ошибке, когда в форме указана карта, срок действия которой истек месяц назад")
    void shouldShowAnErrorMessageBelowTheMonthField_CardExpiredLastMonth() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var generatesMonth = DataHelper.prevMonth();
        formFieldsInfo.setMonth(String.format("|%02d|", generatesMonth.getMonthValue()));
        formFieldsInfo.setYear(String.format("%d", generatesMonth.getYear()).substring(2));
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("Месяц", "Истёк срок действия карты");
    }

    //---ПОЛЕ ГОДА---
    @Feature("Форма")
    @Story("Поле года")
    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/InvalidValuesYear.csv")
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле года заполнено невалидными значениями")
    void shouldCheckWhatCannotBeEnteredInTheYearFieldSomeInvalidValues(String text) {
        var orderCardPage = new OrderCardPage();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.setFieldValue("Год", text);
        String expected = "";
        String actual = formPage.getFieldValue("Год");
        Assertions.assertEquals(expected, actual);
    }

    @Feature("Форма")
    @Story("Поле года")
    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле года не заполнено")
    void shouldShowAnErrorMessageBelowTheYearField_EmptyField() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setYear("");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("Год", "Поле обязательно для заполнения");
    }

    @Feature("Форма")
    @Story("Поле года")
    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле года заполнено нулями")
    void shouldShowAnErrorMessageBelowTheYearField_OnlyZero() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setYear("00");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("Год", "Истёк срок действия карты");
    }

    @Feature("Форма")
    @Story("Поле года")
    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле года заполнено одним символом")
    void shouldShowAnErrorMessageBelowTheYearFieldWhenEnteredOneSymbol() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setYear("3");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("Год", "Неверный формат");
    }

    @Feature("Форма")
    @Story("Поле года")
    @Test
    @DisplayName("Проверяем, что нельзя ввести в поле года более 2-х цифр")
    void canOnlyEnter2DigitsTheYearField() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.setFieldValue("Год", formFieldsInfo.getYear() + "3");
        String expected = formFieldsInfo.getYear();
        String actual = formPage.getFieldValue("Год");
        Assertions.assertEquals(expected, actual);
    }

    @Feature("Форма")
    @Story("Поле года")
    @Test
    @DisplayName("Проверяем выпадающее сообщения об ошибке, когда в форме указана карта, срок действия которой истек год назад")
    void shouldShowAnErrorMessageBelowTheYearField_CardExpiredLastYear() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var generatesYear = DataHelper.prevYear();
        formFieldsInfo.setMonth(String.format("|%02d|", generatesYear.getMonthValue()));
        formFieldsInfo.setYear(String.format("%d", generatesYear.getYear()).substring(2));
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("Год", "Истёк срок действия карты");
    }

    //---ПОЛЕ ВЛАДЕЛЬЦА---
    @Feature("Форма")
    @Story("Поле владельца")
    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/InvalidValuesOwner.csv")
    @DisplayName("Проверяем, что в поле владельца нельзя ввести некоторые невалидные значения")
    void shouldCheckWhatCannotBeEnteredInTheOwnerFieldSomeInvalidValues(String text) {
        var orderCardPage = new OrderCardPage();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.setFieldValue("Владелец", text);
        String expected = "";
        String actual = formPage.getFieldValue("Владелец");
        Assertions.assertEquals(expected, actual);
    }

    @Feature("Форма")
    @Story("Поле владельца")
    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/ValidValuesOwner.csv")
    @DisplayName("Проверяем, что форма отправляется, заполнив поле владельца некоторыми валидными значениями")
    void shouldSuccessfullySubmitFormFilledWithSomeValidValues(String text) {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setOwner(text);
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.goToNotificationPage(formFieldsInfo);
    }

    @Feature("Форма")
    @Story("Поле владельца")
    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле владельца не заполнено")
    void shouldShowAnErrorMessageBelowTheOwnerField_EmptyField() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setOwner("");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("Владелец", "Поле обязательно для заполнения");
    }

    @Feature("Форма")
    @Story("Поле владельца")
    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле владельца заполнено одним символом")
    void shouldShowAnErrorMessageBelowTheOwnerFieldWheEnteredOneSymbol() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setOwner("Q");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("Владелец", "Неверный формат");
    }

    @Feature("Форма")
    @Story("Поле владельца")
    @Test
    @DisplayName("Проверяем, что нельзя ввести в поле владельца более 150-ти симвалов")
    void canOnlyEnter150SymbolsTheOwnerField() {
        var orderCardPage = new OrderCardPage();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.setFieldValue("Владелец", "ANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANN");
        String expected = "ANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAAN";
        String actual = formPage.getFieldValue("Владелец");
        Assertions.assertEquals(expected, actual);
    }

    @Feature("Форма")
    @Story("Поле владельца")
    @Test
    @DisplayName("Проверяем, что при введении в поле владельца символов в нижнем регистре, они будут преобразованы в верхний регистр")
    void shouldUppercaseConversion() {
        var orderCardPage = new OrderCardPage();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.setFieldValue("Владелец", "anna");
        String expected = "ANNA";
        String actual = formPage.getFieldValue("Владелец");
        Assertions.assertEquals(expected, actual);
    }

    //---ПОЛЕ CVC/CVV---
    @Feature("Форма")
    @Story("Поле CVC/CVV")
    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/InvalidValuesCcvCvv.csv")
    @DisplayName("Проверяем, что в поле CVC/CVV нельзя ввести некоторые невалидные значения")
    void shouldCheckWhatCannotBeEnteredInTheCcvCvvFieldSomeInvalidValues(String text) {
        var orderCardPage = new OrderCardPage();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.setFieldValue("CVC/CVV", text);
        String expected = "";
        String actual = formPage.getFieldValue("CVC/CVV");
        Assertions.assertEquals(expected, actual);
    }

    @Feature("Форма")
    @Story("Поле CVC/CVV")
    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле CVC/CVV не заполнено")
    void shouldShowAnErrorMessageBelowTheCcvCvvField_EmptyField() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCcvCvv("");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("CVC/CVV", "Поле обязательно для заполнения");
    }

    @Feature("Форма")
    @Story("Поле CVC/CVV")
    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле CVC/CVV заполнено нулями")
    void shouldShowAnErrorMessageBelowTheCcvCvvField_OnlyZero() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCcvCvv("000");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("CVC/CVV", "Неверный формат");
    }

    @Feature("Форма")
    @Story("Поле CVC/CVV")
    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле CVC/CVV заполнено двумя символами")
    void shouldShowAnErrorMessageBelowTheCcvCvvFieldWheEnteredTwoSymbol() {
        var orderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCcvCvv("23");
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.fillingOutFormFields(formFieldsInfo);
        formPage.formNotSend();
        formPage.errorMessage("CVC/CVV", "Неверный формат");
    }

    @Feature("Форма")
    @Story("Поле CVC/CVV")
    @Test
    @DisplayName("Проверяем, что нельзя ввести в поле CVC/CVV более 3-х цифр")
    void canOnlyEnter3DigitsTheCcvCvvField() {
        var orderCardPage = new OrderCardPage();
        var cardPaymentPage = orderCardPage.goToPaymentPage();
        var formPage = cardPaymentPage.goToFormPage();
        formPage.setFieldValue("CVC/CVV", "4321");
        String expected = "432";
        String actual = formPage.getFieldValue("CVC/CVV");
        Assertions.assertEquals(expected, actual);
    }
}