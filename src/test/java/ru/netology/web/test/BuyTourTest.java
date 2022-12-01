package ru.netology.web.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.OrderCardPage;

import static com.codeborne.selenide.Selenide.open;

public class BuyTourTest {
    @BeforeEach
    void setupTest() {
        open("http://185.119.57.47:8080");
    }

    //---ФУНКЦИОНАЛ КНОПОК---
    @Test
    @DisplayName("Проверяем статус кнопок на изначальной карточке заказа (обе должны быть красного цвета)")
    void shouldCheckButtonStatusOnTheOrderCard() {
        var OrderCardPage = new OrderCardPage();
        OrderCardPage.buttonStatusExtraStart();
    }

    @Test
    @DisplayName("Проверяем статус кнопок при клике на каждую (та, на которую кликнули должна быть белой, другая красной)")
    void shouldCheckButtonsStatus() {
        var OrderCardPage = new OrderCardPage();
        OrderCardPage.buttonStatusExtraPayment();
        OrderCardPage.buttonStatusExtraPaymentOnCredit();
    }

    @Test
    @DisplayName("Проверяем, что при нажатии на конкретную кнопку переходим к нужной форме. Сначала к одной, потом к другой")
    void shouldGoesToTheDesiredFormOnButtonClick() {
        var OrderCardPage = new OrderCardPage();
        OrderCardPage.goToPaymentPage();
        OrderCardPage.goToPaymentOnCreditPage();
    }

    @Test
    @DisplayName("Проверяем, что при нажатии на кнопку «Продолжить», предварительно введя в форму валидные данные, кнопка на некоторое время становится некликабельной")
    void shouldNotBeClickableButtonAfterClick() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.goToNotificationPage(formFieldsInfo);
    }

    //---ОБЩЕЕ---
    @Test
    @DisplayName("Проверяем выпадающее сообщение при покупке тура по карте, когда банк одобрил покупку")
    void shouldSuccessfulSendTheFormOnThePayment() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.goToNotificationPage(formFieldsInfo);
        FormPage.notificationOk();
    }

    @Test
    @DisplayName("Проверяем выпадающее сообщение при покупке тура в кредит, когда банк одобрил кредит")
    void shouldSuccessfulSendTheFormOnThePaymentOnTheCredit() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var CardPaymentOnCreditPage = OrderCardPage.goToPaymentOnCreditPage();
        var FormPage = CardPaymentOnCreditPage.goToFormPage();
        FormPage.goToNotificationPage(formFieldsInfo);
        FormPage.notificationOk();
    }

    @Test
    @DisplayName("Проверяем выпадающее сообщение при покупке тура по карте, когда банк отказал в покупке")
    void shouldBeDeniedPayment() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("4444444444444442");
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.goToNotificationPage(formFieldsInfo);
        FormPage.notificationError();
    }

    @Test
    @DisplayName("Проверяем выпадающее сообщение при покупке тура в кредит, когда банк отказал в выдаче кредита")
    void shouldBeDeniedPaymentOnTheCredit() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("4444444444444442");
        var CardPaymentOnCreditPage = OrderCardPage.goToPaymentOnCreditPage();
        var FormPage = CardPaymentOnCreditPage.goToFormPage();
        FormPage.goToNotificationPage(formFieldsInfo);
        FormPage.notificationError();
    }

    @Test
    @DisplayName("Проверяем, что пустая форма не будет отправлена на сервер")
    void shouldNotSubmitAnEmptyForm() {
        var OrderCardPage = new OrderCardPage();
        var CardPaymentOnCreditPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentOnCreditPage.goToFormPage();
        FormPage.formNotSend();
    }

    //---ВАЛИДАЦИЯ ПОЛЕЙ---
    //---ПОЛЕ НОМЕРА КАРТЫ---
    @ParameterizedTest
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле карты заполнено невалидными значениями")
    @CsvFileSource(files = "src/test/resources/InvalidValuesCardNumber.csv")
    void shouldShowAnErrorMessageBelowTheCardNumberField_InvalidValues(String text) {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber(text);
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("Номер карты", "Неверный формат");
    }

    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле карты не заполнено")
    void shouldShowAnErrorMessageBelowTheCardNumberField_EmptyField() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("");
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("Номер карты", "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле карты заполнено нулями")
    void shouldShowAnErrorMessageBelowTheCardNumberField_OnlyZero() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("0000000000000000");
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("Номер карты", "Неверный формат");
    }

    @Test
    @DisplayName("Проверяем, что введение пробела в поле карты игнорируется")
    void shouldBeSpaceIgnored() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCardNumber("4444 4444 4444 4441");
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.goToNotificationPage(formFieldsInfo);
    }

    @Test
    @DisplayName("Проверяем, что нельзя ввести в поле карты более 16-ти цифр")
    void canOnlyEnter16Digits() {
        var OrderCardPage = new OrderCardPage();
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.setFieldValue("Номер карты", "44444444444444441");
        String expected = "4444 4444 4444 4444";
        String actual = FormPage.getFieldValue("Номер карты");
        Assertions.assertEquals(expected, actual);
    }

    //---ПОЛЕ МЕСЯЦА---
    @ParameterizedTest
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле месяца заполнено невалидными значениями")
    @CsvFileSource(files = "src/test/resources/InvalidValuesMonth.csv")
    void shouldShowAnErrorMessageBelowTheMonthField_InvalidValues(String text) {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setMonth(text);
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("Месяц", "Неверный формат");
    }

    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле месяца не заполнено")
    void shouldShowAnErrorMessageBelowTheMonthField_EmptyField() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setMonth("");
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("Месяц", "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле месяца заполнено нулями")
    void shouldShowAnErrorMessageBelowTheMonthField_OnlyZero() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setMonth("00");
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("Месяц", "Неверный формат");
    }

    @Test
    @DisplayName("Проверяем, что нельзя ввести в поле месяц более 2-х цифр")
    void canOnlyEnter2DigitsTheMonthField() {
        var OrderCardPage = new OrderCardPage();
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.setFieldValue("Месяц", "011");
        String expected = "01";
        String actual = FormPage.getFieldValue("Месяц");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Проверяем, что в поле месяца нельзя указать несуществующий месяцев")
    void shouldShowAnErrorMessageBelowTheMonthField_13Month() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setMonth("13");
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("Месяц", "Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Проверяем выпадающее сообщения об ошибке, когда в форме указана карта, срок действия которой истек месяц назад")
    void shouldShowAnErrorMessageBelowTheMonthField_CardExpiredLastMonth() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var generatesMonth = DataHelper.prevMonth();
        formFieldsInfo.setMonth(String.format("|%02d|", generatesMonth.getMonthValue()));
        formFieldsInfo.setYear(String.format("%d", generatesMonth.getYear()).substring(2));
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("Месяц", "Истёк срок действия карты");
    }

    //---ПОЛЕ ГОДА---
    @ParameterizedTest
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле года заполнено невалидными значениями")
    @CsvFileSource(files = "src/test/resources/InvalidValuesYear.csv")
    void shouldShowAnErrorMessageBelowTheYearField_InvalidValues(String text) {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setYear(text);
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("Год", "Неверный формат");
    }

    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле года не заполнено")
    void shouldShowAnErrorMessageBelowTheYearField_EmptyField() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setYear("");
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("Год", "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле года заполнено нулями")
    void shouldShowAnErrorMessageBelowTheYearField_OnlyZero() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setYear("00");
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("Год", "Истёк срок действия карты");
    }

    @Test
    @DisplayName("Проверяем, что нельзя ввести в поле года более 2-х цифр")
    void canOnlyEnter2DigitsTheYearField() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.setFieldValue("Год", formFieldsInfo.getYear() + "3");
        String expected = formFieldsInfo.getYear();
        String actual = FormPage.getFieldValue("Год");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Проверяем выпадающее сообщения об ошибке, когда в форме указана карта, срок действия которой истек год назад")
    void shouldShowAnErrorMessageBelowTheYearField_CardExpiredLastYear() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        var generatesYear = DataHelper.prevYear();
        formFieldsInfo.setMonth(String.format("|%02d|", generatesYear.getMonthValue()));
        formFieldsInfo.setYear(String.format("%d", generatesYear.getYear()).substring(2));
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("Год", "Истёк срок действия карты");
    }

    //---ПОЛЕ ВЛАДЕЛЬЦА---
    @ParameterizedTest
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле владельца заполнено невалидными значениями")
    @CsvFileSource(files = "src/test/resources/InvalidValuesOwner.csv")
    void shouldShowAnErrorMessageBelowTheOwnerField_InvalidValues(String text) {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setOwner(text);
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("Владелец", "Неверный формат");
    }

    @ParameterizedTest
    @DisplayName("Проверяем, что форма отправляется, заполнив поле владельца некоторыми валидными значениями")
    @CsvFileSource(files = "src/test/resources/ValidValuesOwner.csv")
    void shouldSuccessfullySubmitFormFilledWithSomeValidValues(String text) {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setOwner(text);
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.goToNotificationPage(formFieldsInfo);
    }

    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле владельца не заполнено")
    void shouldShowAnErrorMessageBelowTheOwnerField_EmptyField() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setOwner("");
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("Владелец", "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Проверяем, что нельзя ввести в поле владельца более 150-ти симвалов")
    void canOnlyEnter150SymbolsTheOwnerField() {
        var OrderCardPage = new OrderCardPage();
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.setFieldValue("Владелец", "ANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANN");
        String expected = "ANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAANNAAN";
        String actual = FormPage.getFieldValue("Владелец");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Проверяем, что при введении в поле владельца символов в нижнем регистре, они будут преобразованы в верхний регистр")
    void shouldUppercaseConversion() {
        var OrderCardPage = new OrderCardPage();
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.setFieldValue("Владелец", "anna");
        String expected = "ANNA";
        String actual = FormPage.getFieldValue("Владелец");
        Assertions.assertEquals(expected, actual);
    }

    //---ПОЛЕ CVC/CVV---
    @ParameterizedTest
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле CVC/CVV заполнено невалидными значениями")
    @CsvFileSource(files = "src/test/resources/InvalidValuesCcvCvv.csv")
    void shouldShowAnErrorMessageBelowTheCcvCvvField_InvalidValues(String text) {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCcvCvv(text);
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("CVC/CVV", "Неверный формат");
    }

    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле CVC/CVV не заполнено")
    void shouldShowAnErrorMessageBelowTheCcvCvvField_EmptyField() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCcvCvv("");
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("CVC/CVV", "Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Проверяем выпадающее сообщение об ошибке, когда поле CVC/CVV заполнено нулями")
    void shouldShowAnErrorMessageBelowTheCcvCvvField_OnlyZero() {
        var OrderCardPage = new OrderCardPage();
        var formFieldsInfo = DataHelper.getValidFieldSet();
        formFieldsInfo.setCcvCvv("000");
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.fillingOutFormFields(formFieldsInfo);
        FormPage.formNotSend();
        FormPage.errorMessage("CVC/CVV", "Неверный формат");
    }

    @Test
    @DisplayName("Проверяем, что нельзя ввести в поле CVC/CVV более 3-х цифр")
    void canOnlyEnter3DigitsTheCcvCvvField() {
        var OrderCardPage = new OrderCardPage();
        var CardPaymentPage = OrderCardPage.goToPaymentPage();
        var FormPage = CardPaymentPage.goToFormPage();
        FormPage.setFieldValue("CVC/CVV", "4321");
        String expected = "432";
        String actual = FormPage.getFieldValue("CVC/CVV");
        Assertions.assertEquals(expected, actual);
    }
}